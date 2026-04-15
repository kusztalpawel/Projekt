package org.project.server;

import org.junit.jupiter.api.Test;
import org.project.server.dto.ExamRequestDTO;
import org.project.server.model.Exam;
import org.project.server.repository.ExamRepository;
import org.project.server.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ExamServiceTest {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository examRepository;

    @Test
    void shouldCreateExamForUser() {

        Exam created = null;

        try {
            ExamRequestDTO dto = new ExamRequestDTO();
            dto.setName("Math Exam");
            dto.setPoints(50);
            dto.setGrade(4.5f);
            dto.setType(LocalDate.now());

            created = examService.createExam(dto, 1L);

            assertNotNull(created.getId());
            assertEquals(1L, created.getUser().getId());
        }
        finally {
            if (created != null && created.getId() != null) {
                examRepository.deleteById(created.getId());
            }
        }
    }
}
