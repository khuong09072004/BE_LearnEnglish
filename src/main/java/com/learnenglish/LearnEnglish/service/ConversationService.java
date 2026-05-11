package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnenglish.LearnEnglish.builder.ConversationPromptBuilder;
import com.learnenglish.LearnEnglish.dto.requests.ConversationLessonRequest;
import com.learnenglish.LearnEnglish.dto.requests.ConversationStepRequest;
import com.learnenglish.LearnEnglish.dto.responses.AiEvaluation;
import com.learnenglish.LearnEnglish.dto.responses.ConversationLessonAccessResponse;
import com.learnenglish.LearnEnglish.dto.responses.ConversationLessonListItemResponse;
import com.learnenglish.LearnEnglish.dto.responses.ConversationStepResponse;
import com.learnenglish.LearnEnglish.dto.responses.ConversationTurnResponse;
import com.learnenglish.LearnEnglish.dto.requests.ConversationStepRequest;
import com.learnenglish.LearnEnglish.entity.ConversationLesson;
import com.learnenglish.LearnEnglish.entity.ConversationSession;
import com.learnenglish.LearnEnglish.entity.ConversationStep;
import com.learnenglish.LearnEnglish.entity.ConversationStepAttempt;
import com.learnenglish.LearnEnglish.entity.ConversationTurn;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.exception.AppException;
import com.learnenglish.LearnEnglish.mapper.ConversationTurnMapper;
import com.learnenglish.LearnEnglish.dto.responses.ConversationSessionResponse;
import com.learnenglish.LearnEnglish.repository.ConversationLessonRepository;
import com.learnenglish.LearnEnglish.dto.responses.ConversationLessonResponse;
import com.learnenglish.LearnEnglish.mapper.ConversationLessonMapper;
import com.learnenglish.LearnEnglish.repository.ConversationSessionRepository;
import com.learnenglish.LearnEnglish.repository.ConversationStepAttemptRepository;
import com.learnenglish.LearnEnglish.repository.ConversationStepRepository;
import com.learnenglish.LearnEnglish.repository.ConversationTurnRepository;
import com.learnenglish.LearnEnglish.repository.LevelsRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class ConversationService {

    @Autowired private ConversationSessionRepository sessionRepo;
    @Autowired private ConversationStepRepository stepRepo;
    @Autowired private ConversationTurnRepository turnRepo;
    @Autowired private ConversationStepAttemptRepository stepAttemptRepo;
    @Autowired private OpenAiService openAiService;
    @Autowired private ConversationPromptBuilder promptBuilder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepo;
    @Autowired private ConversationLessonRepository lessonRepo;
    @Autowired private LevelsRepository levelRepo;
    @Autowired private TransactionTemplate transactionTemplate; // Dùng để quản lý luồng thay cho @Transactional

    @Transactional
    public ConversationTurnResponse startLesson(Long lessonId, Long userId) {
        ConversationSession session = new ConversationSession();
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        session.setLesson(lesson);
        session.setUser(user);
        session.setCurrentStep(1);
        session.setIsCompleted(false);
        session.setLearned(false);
        session.setTotalScore(0);
        session.setStartedAt(LocalDateTime.now());
        sessionRepo.save(session);

        ConversationStep step = stepRepo.findByLessonIdAndStepOrder(lessonId, 1)
                .orElseThrow(() -> new RuntimeException("Step 1 not found"));

        AiEvaluation eval = new AiEvaluation();
        eval.setCorrect(true);
        eval.setScore(0.0);
        eval.setAnalysis(null);
        eval.setCorrection(null);
        eval.setNextMessage(step.getSampleAnswer());

        ConversationTurn aiTurn = ConversationTurn.builder()
                .session(session)
                .role(ConversationTurn.Role.AI)
                .content(eval.getNextMessage())
                .analysis(null)
                .correction(null)
                .score(0)
                .createdAt(LocalDateTime.now())
                .build();
        turnRepo.save(aiTurn);

        return ConversationTurnMapper.toDto(aiTurn);
    }

    public java.util.Map<String,Object> generateStepSuggestions(Long lessonId, Integer count) {
        ensureLessonExists(lessonId);
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        int n = (count == null || count <= 0) ? 3 : count;
        String prompt = buildStepSuggestionPrompt(lesson, n);

        String aiRaw = openAiService.askAI(prompt);
        String raw = aiRaw;
        try {
            String jsonOnly = cleanAiJson(aiRaw);
            ConversationStepRequest[] arr = objectMapper.readValue(jsonOnly, ConversationStepRequest[].class);
            java.util.List<ConversationStepRequest> suggestions = java.util.Arrays.asList(arr);
            return java.util.Map.of("suggestions", suggestions, "raw", raw);
        } catch (Exception e) {
            return java.util.Map.of("suggestions", java.util.List.of(), "raw", raw, "error", e.getMessage());
        }
    }

    private String buildStepSuggestionPrompt(ConversationLesson lesson, int count) {
        StringBuilder sb = new StringBuilder();
        sb.append("Generate ").append(count).append(" JSON steps for a conversation lesson. ");
        sb.append("Return a JSON array where each element has fields: stepOrder (int), aiRole (string), userTask (string), grammarFocus (string), sampleAnswer (string), maxAttempts (int).\\n");
        sb.append("Use the lesson content as the main source of truth.\\n");
        sb.append("Lesson title: \"").append(lesson.getTitle() != null ? lesson.getTitle() : "").append("\".\\n");
        sb.append("Lesson description: \"").append(lesson.getDescription() != null ? lesson.getDescription() : "").append("\".\\n");
        sb.append("Lesson goal: \"").append(lesson.getGoal() != null ? lesson.getGoal() : "").append("\".\\n");
        sb.append("Lesson system prompt: \"").append(lesson.getSystemPrompt() != null ? lesson.getSystemPrompt() : "").append("\".\\n");
        sb.append("Skill focus: \"").append(lesson.getSkillFocus() != null ? lesson.getSkillFocus().name() : "SPEAKING").append("\".\\n");
        sb.append("Create steps that follow the lesson system prompt, match the title and goal, and feel like a natural progression from step 1 to step ").append(count).append(".\\n");
        sb.append("Each step should build on the previous one instead of being random or repetitive.\\n");
        sb.append("Make answers concise and appropriate for the level. Number the steps starting at 1.\\n");
        sb.append("Example output:\\n[\\n  {\\\"stepOrder\\\":1,\\\"aiRole\\\":\\\"Barista\\\",\\\"userTask\\\":\\\"Greet the customer\\\",\\\"grammarFocus\\\":\\\"Greetings\\\",\\\"sampleAnswer\\\":\\\"Hello, what can I get for you?\\\",\\\"maxAttempts\\\":3},\\n  {\\\"stepOrder\\\":2,...}\\n]\\n");
        return sb.toString();
    }

    // KHÔNG dùng @Transactional ở hàm này
    public ConversationTurnResponse reply(Long sessionId, String userMessage) {

        // 1️⃣ BƯỚC 1: LƯU DB LẦN 1 (Lưu User Turn & Lấy ngữ cảnh)
        ConversationContext context = transactionTemplate.execute(status -> {
            ConversationSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

            if (session.getIsCompleted()) {
                throw new AppException(
                    "conversation_completed",
                    "Conversation already completed",
                    HttpStatus.CONFLICT
                );
            }

            ConversationStep step = stepRepo
                .findByLessonIdAndStepOrder(session.getLesson().getId(), session.getCurrentStep())
                .orElseThrow(() -> new RuntimeException("Step not found"));

            ConversationStepAttempt attempt = stepAttemptRepo
                .findBySessionIdAndStepId(session.getId(), step.getId())
                .orElseGet(() -> stepAttemptRepo.save(
                    ConversationStepAttempt.builder()
                        .session(session)
                        .step(step)
                        .attemptCount(0)
                        .passed(false)
                        .assistMode(false)
                        .build()
                ));

            // Lưu USER turn
            turnRepo.save(ConversationTurn.builder()
                .session(session)
                .role(ConversationTurn.Role.USER)
                .content(userMessage)
                .createdAt(LocalDateTime.now())
                .build()
            );

            // Lấy lịch sử chat để nhét vào prompt
            List<ConversationTurn> recentTurns = turnRepo.findTop10BySessionIdOrderByCreatedAtAsc(sessionId);
            String history = recentTurns.stream()
                .limit(Math.max(0, recentTurns.size() - 1)) // Bỏ qua câu user vừa gửi
                .map(t -> t.getRole() + ": " + t.getContent())
                .collect(Collectors.joining("\n"));

            return new ConversationContext(session, step, attempt, history);
        });

        if (context == null) throw new RuntimeException("Failed to prepare conversation context");

        // 2️⃣ BƯỚC 2: GỌI AI (Database Connection đã được giải phóng hoàn toàn lúc này)
        String prompt = promptBuilder.build(context.step(), context.attempt(), userMessage, context.history());
        
        AiEvaluation eval = null;
        int maxRetries = 2; // Thử lại 2 lần nếu AI trả về JSON lỗi

        for (int i = 0; i < maxRetries; i++) {
            try {
                String aiRaw = openAiService.askAI(prompt);
                System.out.println("AI RAW RESPONSE (Attempt " + (i + 1) + "): \n" + aiRaw);
                String jsonOnly = cleanAiJson(aiRaw);
                eval = objectMapper.readValue(jsonOnly, AiEvaluation.class);
                break; // Nếu parse thành công thì thoát vòng lặp
            } catch (Exception e) {
                if (i == maxRetries - 1) {
                    throw new RuntimeException("AI failed to return valid JSON after " + maxRetries + " attempts", e);
                }
            }
        }

        final AiEvaluation finalEval = eval;

        // 3️⃣ BƯỚC 3: LƯU DB LẦN 2 (Lưu kết quả AI)
        ConversationTurn aiTurn = transactionTemplate.execute(status -> {
            
            // Cập nhật attempt
            ConversationStepAttempt attempt = context.attempt();
            attempt.setAttemptCount(attempt.getAttemptCount() + 1);
            attempt.setLastAttemptAt(LocalDateTime.now());

            boolean pass = finalEval.isCorrect();
            boolean exceedLimit = attempt.getAttemptCount() >= context.step().getMaxAttempts();

            attempt.setPassed(pass);
            attempt.setAssistMode(!pass && exceedLimit);
            stepAttemptRepo.save(attempt);

            // Chuyển step hoặc hoàn thành
            ConversationSession session = context.session();
            if (pass || exceedLimit) {
                session.setCurrentStep(session.getCurrentStep() + 1);
                if (!stepRepo.existsByLessonIdAndStepOrder(session.getLesson().getId(), session.getCurrentStep())) {
                    session.setIsCompleted(true);
                    session.setLearned(true);
                    session.setFinishedAt(LocalDateTime.now());
                }
                sessionRepo.save(session);
            }

            int scaledScore = (int) Math.round(finalEval.getScore() * 10);
            
            // Convert Object/String sang JsonNode để phù hợp với Entity của bạn
            JsonNode analysisNode = finalEval.getAnalysis() != null ? objectMapper.valueToTree(finalEval.getAnalysis()) : null;
            JsonNode correctionNode = finalEval.getCorrection() != null ? objectMapper.valueToTree(finalEval.getCorrection()) : null;

            // Lưu AI Turn
            return turnRepo.save(
                ConversationTurn.builder()
                    .session(session)
                    .role(ConversationTurn.Role.AI)
                    .content(finalEval.getNextMessage())
                    .analysis(analysisNode)
                    .correction(correctionNode)
                    .score(scaledScore)
                    .createdAt(LocalDateTime.now())
                    .build()
            );
        });

        return ConversationTurnMapper.toDto(aiTurn);
    }

    public ConversationSession getSession(Long sessionId) {
        return sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public ConversationLessonResponse getLesson(Long lessonId) {
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        java.util.List<ConversationStep> steps = stepRepo.findByLessonIdOrderByStepOrder(lessonId);
        return ConversationLessonMapper.toDto(lesson, steps);
    }

    public ConversationLessonAccessResponse getLessonForUser(Long lessonId, Long userId) {
        ConversationLessonResponse lesson = getLesson(lessonId);
        var learnedSessionOpt = sessionRepo.findTopByUserIdAndLessonIdAndLearnedTrueOrderByFinishedAtDesc(userId, lessonId);

        if (learnedSessionOpt.isPresent()) {
            ConversationSessionResponse learned = toSessionResponse(learnedSessionOpt.get());
            return ConversationLessonAccessResponse.builder()
                    .lesson(lesson)
                    .isLearn(true)
                    .learnedSessionId(learned.getId())
                    .history(learned)
                    .build();
        }

        return ConversationLessonAccessResponse.builder()
                .lesson(lesson)
                .isLearn(false)
                .learnedSessionId(null)
                .history(null)
                .build();
    }

    public ConversationSessionResponse getLearnedHistoryByLesson(Long lessonId, Long userId) {
        ConversationSession session = sessionRepo.findTopByUserIdAndLessonIdAndLearnedTrueOrderByFinishedAtDesc(userId, lessonId)
                .orElseThrow(() -> new RuntimeException("User has not completed this lesson yet"));
        return toSessionResponse(session);
    }

    public java.util.List<ConversationLessonResponse> getAllLessons() {
        java.util.List<ConversationLesson> lessons = lessonRepo.findAll();
        java.util.List<ConversationLessonResponse> resp = new java.util.ArrayList<>();
        for (ConversationLesson lesson : lessons) {
            java.util.List<ConversationStep> steps = stepRepo.findByLessonIdOrderByStepOrder(lesson.getId());
            resp.add(ConversationLessonMapper.toDto(lesson, steps));
        }
        return resp;
    }

    public java.util.List<ConversationLessonListItemResponse> getAllLessonsForUser(Long userId) {
        java.util.List<ConversationLesson> lessons = lessonRepo.findAll();
        return lessons.stream()
                .map(lesson -> ConversationLessonListItemResponse.builder()
                        .id(lesson.getId())
                        .title(lesson.getTitle())
                        .description(lesson.getDescription())
                        .levelId(lesson.getLevel() != null ? lesson.getLevel().getId() : null)
                        .levelName(lesson.getLevel() != null ? lesson.getLevel().getName() : null)
                        .skillFocus(lesson.getSkillFocus() != null ? lesson.getSkillFocus().name() : null)
                        .goal(lesson.getGoal())
                        .createdAt(lesson.getCreatedAt())
                        .isLearn(sessionRepo.existsByUserIdAndLessonIdAndLearnedTrue(userId, lesson.getId()))
                        .build())
                .collect(java.util.stream.Collectors.toList());
    }

    public List<ConversationStepResponse> getStepsByLesson(Long lessonId) {
        ensureLessonExists(lessonId);
        return stepRepo.findByLessonIdOrderByStepOrder(lessonId)
                .stream()
                .map(this::toStepResponse)
                .collect(Collectors.toList());
    }

    public ConversationStepResponse getStepByLessonAndId(Long lessonId, Long stepId) {
        ConversationStep step = getStepInLesson(lessonId, stepId);
        return toStepResponse(step);
    }

    @Transactional
    public ConversationStepResponse createStep(Long lessonId, ConversationStepRequest request) {
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Integer requestedOrder = request.getStepOrder();
        if (requestedOrder == null || requestedOrder < 1) {
            requestedOrder = stepRepo.findByLessonIdOrderByStepOrder(lessonId).size() + 1;
        }

        // Tạo step với stepOrder tạm thời (giá trị âm để tránh collision)
        ConversationStep step = ConversationStep.builder()
                .lesson(lesson)
                .stepOrder(-1)
                .aiRole(request.getAiRole())
                .userTask(request.getUserTask())
                .grammarFocus(request.getGrammarFocus())
                .sampleAnswer(request.getSampleAnswer())
                .maxAttempts(request.getMaxAttempts() != null && request.getMaxAttempts() > 0 ? request.getMaxAttempts() : 3)
                .createdAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now())
                .build();

        ConversationStep saved = stepRepo.save(step);
        
        // Set stepOrder tạm thời dùng negative stepId, rồi set giá trị mong muốn
        saved.setStepOrder(-(int) (saved.getId() % Integer.MAX_VALUE));
        stepRepo.save(saved);
        
        // Set stepOrder mong muốn rồi normalize
        saved.setStepOrder(requestedOrder);
        stepRepo.save(saved);
        normalizeStepOrders(lessonId);
        
        ConversationStep normalized = stepRepo.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Step not found"));
        return toStepResponse(normalized);
    }

    @Transactional

    public ConversationStepResponse updateStep(Long lessonId, Long stepId, ConversationStepRequest request) {
        ConversationStep step = getStepInLesson(lessonId, stepId);

        // Đổi stepOrder thành giá trị tạm thời (negative stepId) để tránh unique constraint collision
        step.setStepOrder(-(int) (stepId % Integer.MAX_VALUE));
        stepRepo.save(step);

        // Giờ set giá trị thực từ request
        if (request.getStepOrder() != null && request.getStepOrder() > 0) {
            step.setStepOrder(request.getStepOrder());
        }
        if (request.getAiRole() != null) {
            step.setAiRole(request.getAiRole());
        }
        if (request.getUserTask() != null) {
            step.setUserTask(request.getUserTask());
        }
        if (request.getGrammarFocus() != null) {
            step.setGrammarFocus(request.getGrammarFocus());
        }
        if (request.getSampleAnswer() != null) {
            step.setSampleAnswer(request.getSampleAnswer());
        }
        if (request.getMaxAttempts() != null && request.getMaxAttempts() > 0) {
            step.setMaxAttempts(request.getMaxAttempts());
        }
        if (request.getCreatedAt() != null) {
            step.setCreatedAt(request.getCreatedAt());
        }

        ConversationStep saved = stepRepo.save(step);
        normalizeStepOrders(lessonId);
        ConversationStep normalized = stepRepo.findById(saved.getId())
                .orElseThrow(() -> new RuntimeException("Step not found"));
        return toStepResponse(normalized);
    }

    @Transactional
    public void deleteStep(Long lessonId, Long stepId) {
        ConversationStep step = getStepInLesson(lessonId, stepId);
        stepAttemptRepo.deleteByStepId(stepId);
        stepRepo.delete(step);
        normalizeStepOrders(lessonId);
    }

    @Transactional
    public List<ConversationStepResponse> createStepsBulk(Long lessonId, List<ConversationStepRequest> requests) {
        ensureLessonExists(lessonId);
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<ConversationStep> steps = new ArrayList<>();
        for (ConversationStepRequest req : requests) {
            ConversationStep step = ConversationStep.builder()
                    .lesson(lesson)
                    .stepOrder(req.getStepOrder())
                    .aiRole(req.getAiRole())
                    .userTask(req.getUserTask())
                    .grammarFocus(req.getGrammarFocus())
                    .sampleAnswer(req.getSampleAnswer())
                    .maxAttempts(req.getMaxAttempts() != null && req.getMaxAttempts() > 0 ? req.getMaxAttempts() : 3)
                    .createdAt(req.getCreatedAt() != null ? req.getCreatedAt() : LocalDateTime.now())
                    .build();
            steps.add(step);
        }

        List<ConversationStep> saved = stepRepo.saveAll(steps);
        normalizeStepOrders(lessonId);
        
        List<ConversationStep> normalized = stepRepo.findByLessonIdOrderByStepOrder(lessonId);
        return normalized.stream()
                .map(this::toStepResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConversationLessonResponse createLesson(ConversationLessonRequest request) {
        ConversationLesson lesson = new ConversationLesson();
        applyLessonRequest(lesson, request);
        lesson.setCreatedAt(request.getCreatedAt() != null ? request.getCreatedAt() : LocalDateTime.now());
        ConversationLesson savedLesson = lessonRepo.save(lesson);
        return ConversationLessonMapper.toDto(savedLesson, stepRepo.findByLessonIdOrderByStepOrder(savedLesson.getId()));
    }

    @Transactional
    public ConversationLessonResponse updateLesson(Long lessonId, ConversationLessonRequest request) {
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));
        applyLessonRequest(lesson, request);
        if (request.getCreatedAt() != null) {
            lesson.setCreatedAt(request.getCreatedAt());
        }
        ConversationLesson savedLesson = lessonRepo.save(lesson);
        return ConversationLessonMapper.toDto(savedLesson, stepRepo.findByLessonIdOrderByStepOrder(lessonId));
    }

    @Transactional
    public void deleteLesson(Long lessonId) {
        ConversationLesson lesson = lessonRepo.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        List<ConversationSession> sessions = sessionRepo.findByLessonIdOrderByStartedAtDesc(lessonId);
        for (ConversationSession session : sessions) {
            turnRepo.deleteBySessionId(session.getId());
            stepAttemptRepo.deleteBySessionId(session.getId());
        }
        sessionRepo.deleteAll(sessions);

        List<ConversationStep> steps = stepRepo.findByLessonIdOrderByStepOrder(lessonId);
        stepRepo.deleteAll(steps);
        lessonRepo.delete(lesson);
    }

    public List<ConversationSessionResponse> getSessionsByUser(Long userId) {
        List<ConversationSession> sessions = sessionRepo.findByUserIdOrderByStartedAtDesc(userId);
        return sessions.stream().map(this::toSessionResponse).collect(java.util.stream.Collectors.toList());
    }

    public List<ConversationSessionResponse> getAllSessions() {
        List<ConversationSession> sessions = sessionRepo.findAllByOrderByStartedAtDesc();
        return sessions.stream().map(this::toSessionResponse).collect(java.util.stream.Collectors.toList());
    }

    public ConversationSessionResponse getSessionDetail(Long sessionId) {
        ConversationSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return toSessionResponse(session);
    }

    public List<ConversationTurnResponse> getSessionTurns(Long sessionId) {
        ensureSessionExists(sessionId);
        return turnRepo.findBySessionIdOrderByCreatedAtAsc(sessionId).stream()
                .map(ConversationTurnMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    private String cleanAiJson(String raw) {
        raw = raw.trim();
        if (raw.startsWith("```")) {
            raw = raw.replaceAll("^```json", "")
                     .replaceAll("^```", "")
                     .replaceAll("```$", "")
                     .trim();
        }
        int start = raw.indexOf("{");
        int end = raw.lastIndexOf("}");
        if (start == -1 || end == -1) {
            throw new RuntimeException("No JSON object found in AI response");
        }
        return raw.substring(start, end + 1);
    }

    private void applyLessonRequest(ConversationLesson lesson, ConversationLessonRequest request) {
        if (request.getTitle() != null) lesson.setTitle(request.getTitle());
        if (request.getDescription() != null) lesson.setDescription(request.getDescription());
        if (request.getLevelId() != null) {
            lesson.setLevel(levelRepo.findById(request.getLevelId())
                    .orElseThrow(() -> new RuntimeException("Level not found")));
        } else if (lesson.getLevel() == null) {
            lesson.setLevel(levelRepo.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No level available for conversation lesson")));
        }
        if (lesson.getSkillFocus() == null) lesson.setSkillFocus(ConversationLesson.SkillFocus.SPEAKING);
        if (request.getGoal() != null) {
            lesson.setGoal(request.getGoal());
        } else if (lesson.getGoal() == null || lesson.getGoal().isBlank()) {
            throw new RuntimeException("Goal is required");
        }
        if (request.getSystemPrompt() != null) {
            lesson.setSystemPrompt(request.getSystemPrompt());
        } else if (lesson.getSystemPrompt() == null || lesson.getSystemPrompt().isBlank()) {
            throw new RuntimeException("System prompt is required");
        }
    }

    private void ensureDefaultStep(ConversationLesson lesson) {
        List<ConversationStep> steps = stepRepo.findByLessonIdOrderByStepOrder(lesson.getId());
        if (!steps.isEmpty()) {
            return;
        }

        ConversationStep defaultStep = ConversationStep.builder()
                .lesson(lesson)
                .stepOrder(1)
                .aiRole("AI conversation partner")
                .userTask("Introduce yourself and answer the topic question.")
                .grammarFocus(lesson.getSkillFocus() != null ? lesson.getSkillFocus().name() : "SPEAKING")
                .sampleAnswer("Hello! Let's practice this topic together.")
                .maxAttempts(3)
                .createdAt(LocalDateTime.now())
                .build();
        stepRepo.save(defaultStep);
    }

    private void ensureLessonExists(Long lessonId) {
        if (!lessonRepo.existsById(lessonId)) {
            throw new RuntimeException("Lesson not found");
        }
    }

    private ConversationStep getStepInLesson(Long lessonId, Long stepId) {
        ConversationStep step = stepRepo.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Step not found"));
        if (step.getLesson() == null || !lessonId.equals(step.getLesson().getId())) {
            throw new RuntimeException("Step does not belong to lesson");
        }
        return step;
    }

    private void normalizeStepOrders(Long lessonId) {
        List<ConversationStep> steps = stepRepo.findByLessonIdOrderByStepOrder(lessonId);
        steps.sort(java.util.Comparator
                .comparing(ConversationStep::getStepOrder, java.util.Comparator.nullsLast(Integer::compareTo))
                .thenComparing(ConversationStep::getId));
        int order = 1;
        for (ConversationStep step : steps) {
            step.setStepOrder(order++);
        }
        stepRepo.saveAll(steps);
    }

    private ConversationStepResponse toStepResponse(ConversationStep step) {
        return ConversationStepResponse.builder()
                .id(step.getId())
                .stepOrder(step.getStepOrder())
                .aiRole(step.getAiRole())
                .userTask(step.getUserTask())
                .grammarFocus(step.getGrammarFocus())
                .sampleAnswer(step.getSampleAnswer())
                .maxAttempts(step.getMaxAttempts())
                .createdAt(step.getCreatedAt())
                .build();
    }

    private String buildDefaultGoal(String title) {
        if (title == null || title.isBlank()) {
            return "Practice a guided conversation.";
        }
        return "Practice a guided conversation about " + title.trim() + ".";
    }

    private String buildDefaultSystemPrompt(String title, String goal) {
        String topic = (title == null || title.isBlank()) ? "the topic" : title.trim();
        return "You are a friendly English tutor. Guide the learner through a conversation about "
                + topic + ". Goal: " + goal + ". Keep replies concise, helpful, and natural.";
    }

    private ConversationSessionResponse toSessionResponse(ConversationSession session) {
        List<ConversationTurnResponse> turns = turnRepo.findBySessionIdOrderByCreatedAtAsc(session.getId())
                .stream()
                .map(ConversationTurnMapper::toDto)
                .collect(java.util.stream.Collectors.toList());

        return ConversationSessionResponse.builder()
                .id(session.getId())
                .lessonId(session.getLesson() != null ? session.getLesson().getId() : null)
                .lessonTitle(session.getLesson() != null ? session.getLesson().getTitle() : null)
                .userId(session.getUser() != null ? session.getUser().getId() : null)
                .userName(session.getUser() != null ? session.getUser().getFullName() : null)
                .currentStep(session.getCurrentStep())
                .isCompleted(session.getIsCompleted())
                .isLearn(session.getLearned())
                .totalScore(session.getTotalScore())
                .startedAt(session.getStartedAt())
                .finishedAt(session.getFinishedAt())
                .turns(turns)
                .build();
    }

    private void ensureSessionExists(Long sessionId) {
        if (!sessionRepo.existsById(sessionId)) {
            throw new RuntimeException("Session not found");
        }
    }

    // Record nội bộ giữ dữ liệu truyền giữa các Transactional block
    private record ConversationContext(
        ConversationSession session, 
        ConversationStep step, 
        ConversationStepAttempt attempt, 
        String history
    ) {}
}