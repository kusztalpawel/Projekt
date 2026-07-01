package org.project.server.dto;

import java.util.List;

public record ExamQuestionDTO(Long questionId, Long examId, String text, Integer points, List<ExamAnswerDTO> answers) {
}
