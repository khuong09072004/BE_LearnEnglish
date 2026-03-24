package com.learnenglish.LearnEnglish.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnenglish.LearnEnglish.builder.ConversationPromptBuilder;
import com.learnenglish.LearnEnglish.dto.responses.AiEvaluation;
import com.learnenglish.LearnEnglish.dto.responses.ConversationTurnResponse;
import com.learnenglish.LearnEnglish.entity.ConversationLesson;
import com.learnenglish.LearnEnglish.entity.ConversationSession;
import com.learnenglish.LearnEnglish.entity.ConversationStep;
import com.learnenglish.LearnEnglish.entity.ConversationStepAttempt;
import com.learnenglish.LearnEnglish.entity.ConversationTurn;
import com.learnenglish.LearnEnglish.entity.User;
import com.learnenglish.LearnEnglish.mapper.ConversationTurnMapper;
import com.learnenglish.LearnEnglish.repository.ConversationLessonRepository;
import com.learnenglish.LearnEnglish.repository.ConversationSessionRepository;
import com.learnenglish.LearnEnglish.repository.ConversationStepAttemptRepository;
import com.learnenglish.LearnEnglish.repository.ConversationStepRepository;
import com.learnenglish.LearnEnglish.repository.ConversationTurnRepository;
import com.learnenglish.LearnEnglish.repository.UserRepository;

@Service
public class ConversationService {

    @Autowired private ConversationSessionRepository sessionRepo;
    @Autowired private ConversationStepRepository stepRepo;
    @Autowired private ConversationTurnRepository turnRepo;
    @Autowired private ConversationStepAttemptRepository stepAttemptRepo;
    @Autowired private GeminiService geminiService;
    @Autowired private ConversationPromptBuilder promptBuilder;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepo;
    @Autowired private ConversationLessonRepository lessonRepo;
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

    // KHÔNG dùng @Transactional ở hàm này
    public ConversationTurnResponse reply(Long sessionId, String userMessage) {

        // 1️⃣ BƯỚC 1: LƯU DB LẦN 1 (Lưu User Turn & Lấy ngữ cảnh)
        ConversationContext context = transactionTemplate.execute(status -> {
            ConversationSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

            if (session.getIsCompleted()) {
                throw new RuntimeException("Conversation already completed");
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
                String aiRaw = geminiService.askAI(prompt);
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

    // Record nội bộ giữ dữ liệu truyền giữa các Transactional block
    private record ConversationContext(
        ConversationSession session, 
        ConversationStep step, 
        ConversationStepAttempt attempt, 
        String history
    ) {}
}