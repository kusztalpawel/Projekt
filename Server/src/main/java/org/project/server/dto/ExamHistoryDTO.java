package org.project.server.dto;

import java.time.LocalDateTime;

public record ExamHistoryDTO(Long attemptId, String examName, int correctAnswers, int totalQuestions, int points, int totalPoints, double percentage, double grade, LocalDateTime finishedAt) {
}
