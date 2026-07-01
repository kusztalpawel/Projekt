package org.project.server.dto;

import java.util.Map;

public record ExamTestQuestionDTO(Long questionId, Long examId, String text, Integer points, Map<Long, String> answers) {
}
