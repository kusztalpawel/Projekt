package org.project.server.controller;

import org.project.server.dto.ExamRequestDTO;
import org.project.server.dto.ExamResponseDTO;
import org.project.server.mapper.ExamMapper;
import org.project.server.model.Exam;
import org.project.server.service.ExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamResponseDTO> create(@RequestBody ExamRequestDTO dto) {

        Long userId = 1L;  //TODO: pobierz z auth

        Exam exam = examService.createExam(dto, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ExamMapper.toDTO(exam));
    }

    @GetMapping
    public ResponseEntity<List<ExamResponseDTO>> getAll() {
        List<ExamResponseDTO> result = examService.getAll()
                .stream()
                .map(ExamMapper::toDTO)
                .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> getById(@PathVariable Long id) {
        Exam exam = examService.getById(id);
        return ResponseEntity.ok(ExamMapper.toDTO(exam));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody ExamRequestDTO dto) {
        Exam updated = examService.update(id, dto);
        return ResponseEntity.ok(ExamMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        examService.delete(id);
        return ResponseEntity.noContent().build();
    }
}