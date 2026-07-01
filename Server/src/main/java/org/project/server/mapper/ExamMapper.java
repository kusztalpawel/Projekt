package org.project.server.mapper;

import org.project.server.dto.*;
import org.project.server.model.Exam;
import org.project.server.model.ExamAnswer;
import org.project.server.model.ExamAttempt;
import org.project.server.model.ExamQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExamMapper {
    private ExamMapper() {}


    public static Exam toEntity(ExamDTO dto) {
        Exam exam = new Exam();
        exam.setName(dto.name());
        exam.setCode(dto.code());
        exam.setTimeLimit(dto.timeLimit());

        return exam;
    }

    public static ExamDTO toDTO(Exam exam) {
        return new ExamDTO(exam.getId(), exam.getName(), exam.getCode(), exam.getTimeLimit());
    }

    public static ExamResponseDTO toResponseDTO(ExamAttempt examAttempt, Exam exam) {
        return new ExamResponseDTO(examAttempt.getId(), examAttempt.getStartedAt(), exam.getId(), exam.getName(), exam.getTimeLimit(), examAttempt.isBonusMinutesUsed(), examAttempt.isBonusPointsUsed(), examAttempt.isRemoveOptionUsed());
    }

    public static ExamCreatedDTO createToDTO(Exam exam) {
        return new ExamCreatedDTO(exam.getId(), exam.getName(), exam.getTimeLimit());
    }

    public static ExamQuestionDTO toQuestionDTO(ExamQuestion examQuestion){
        return new ExamQuestionDTO(examQuestion.getId(), examQuestion.getExam().getId(), examQuestion.getText(), examQuestion.getPoints(), toAnswersListDTO(examQuestion.getAnswers()));
    }

    public static ExamTestQuestionDTO toTestQuestionDTO(ExamQuestion examQuestion){
        return new ExamTestQuestionDTO(examQuestion.getId(), examQuestion.getExam().getId(), examQuestion.getText(), examQuestion.getPoints(), examQuestion.getAnswers().stream().collect(Collectors.toMap(ExamAnswer::getId, ExamAnswer::getText)));
    }

    public static ExamAnswerDTO toAnswerDTO(ExamAnswer examAnswer){
        return new ExamAnswerDTO(examAnswer.getId(), examAnswer.getText(), examAnswer.getCorrect());
    }

    public static List<ExamAnswerDTO> toAnswersListDTO(List<ExamAnswer> examAnswers){
        List<ExamAnswerDTO> list = new ArrayList<>();
        for (ExamAnswer examAnswer : examAnswers) {
            list.add(toAnswerDTO(examAnswer));
        }

        return list;
    }

    public static List<ExamQuestionDTO> toQuestionsListDTO(List<ExamQuestion> byExam) {
        List<ExamQuestionDTO> list = new ArrayList<>();
        for (ExamQuestion examQuestion : byExam) {
            list.add(toQuestionDTO(examQuestion));
        }

        return list;
    }

    public static List<ExamTestQuestionDTO> toTestQuestionsListDTO(List<ExamQuestion> byExam) {
        List<ExamTestQuestionDTO> list = new ArrayList<>();
        for (ExamQuestion examQuestion : byExam) {
            list.add(toTestQuestionDTO(examQuestion));
        }

        return list;
    }
}