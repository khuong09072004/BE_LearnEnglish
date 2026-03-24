package com.learnenglish.LearnEnglish.builder;

import org.springframework.stereotype.Component;

import com.learnenglish.LearnEnglish.entity.ConversationStep;
import com.learnenglish.LearnEnglish.entity.ConversationStepAttempt;

@Component
public class ConversationPromptBuilder {

    public String build(
            ConversationStep step,
            ConversationStepAttempt attempt,
            String userMessage,
            String chatHistory) {

        return """
                You are an AI tutor role-playing a conversation.
                
                AI ROLE:
                %s
                
                USER TASK:
                %s
                
                GRAMMAR FOCUS:
                %s
                
                RULES:
                - Meaning > grammar
                - correct=true → correction=null
                - correct=false → correction required
                - If max attempts reached → guide forward
                
                CONVERSATION HISTORY (Do not evaluate these, just use for context):
                %s
                
                CURRENT USER MESSAGE (Evaluate this):
                %s
                
                ATTEMPT:
                %d / %d
                
                OUTPUT (JSON ONLY):
                {
                  "correct": boolean,
                  "score": number, // MUST be a number from 0.0 to 10.0
                  "analysis": string,
                  "correction": {
                    "fixed_sentence": string,
                    "explanation": string
                  } | null,
                  "nextMessage": string
                }
                """
                .formatted(
                        step.getAiRole(),
                        step.getUserTask(),
                        step.getGrammarFocus(),
                        chatHistory.isBlank() ? "(No history yet)" : chatHistory,
                        userMessage,
                        attempt.getAttemptCount() + 1,
                        step.getMaxAttempts());
    }
}