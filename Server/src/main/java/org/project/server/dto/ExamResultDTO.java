package org.project.server.dto;

public record ExamResultDTO(int correctAnswers, int totalQuestions, int points, int totalPoints, double percentage, double grade) {
}
