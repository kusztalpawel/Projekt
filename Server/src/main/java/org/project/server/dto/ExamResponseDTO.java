package org.project.server.dto;

import java.time.LocalDateTime;

public record ExamResponseDTO(Long attemptId, LocalDateTime startedAt, Long examId, String name, Integer timeLimit, boolean bonusMinutesUsed, boolean bonusPointsUsed, boolean removeOptionUsed) {
}