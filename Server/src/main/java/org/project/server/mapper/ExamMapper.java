package org.project.server.mapper;

import jakarta.persistence.*;
import org.project.server.dto.ExamRequestDTO;
import org.project.server.dto.ExamResponseDTO;
import org.project.server.model.Exam;

public class ExamMapper {

    public static Exam toEntity(ExamRequestDTO dto) {
        Exam exam = new Exam();
        exam.setName(dto.getName());
        exam.setType(dto.getType());
        exam.setPoints(dto.getPoints());
        exam.setGrade(dto.getGrade());
        return exam;
    }

    public static ExamResponseDTO toDTO(Exam exam) {
        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setId(exam.getId());
        dto.setName(exam.getName());
        dto.setType(exam.getType());
        dto.setPoints(exam.getPoints());
        dto.setGrade(exam.getGrade());
        dto.setUserId(exam.getUser().getId());
        return dto;
    }
}