package org.project.server.controller;

import org.project.server.dto.*;
import org.project.server.mapper.ExamMapper;
import org.project.server.model.Exam;
import org.project.server.service.ExamQuestionService;
import org.project.server.service.ExamService;
import org.project.server.service.PowerUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exams")
public class ExamController {
    private final ExamService examService;
    private final ExamQuestionService questionService;
    private final PowerUpService powerUpService;

    public ExamController(ExamService examService, ExamQuestionService questionService, PowerUpService powerUpService) {
        this.examService = examService;
        this.questionService = questionService;
        this.powerUpService = powerUpService;
    }

    @PostMapping
    public ResponseEntity<ExamCreatedDTO> create(@RequestBody ExamDTO dto, Authentication authentication) {
        return ResponseEntity.ok(ExamMapper.createToDTO(examService.create(dto, authentication.getName())));
    }

    @GetMapping
    public ResponseEntity<List<Exam>> getAll() {
        return ResponseEntity.ok(examService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exam> getById(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getById(id));
    }

    @PostMapping("/question")
    public ResponseEntity<ExamQuestionDTO> createQuestion(@RequestBody ExamQuestionDTO dto) {
        return ResponseEntity.ok(ExamMapper.toQuestionDTO(questionService.create(dto)));
    }

    @GetMapping("/questions/{examId}")
    public ResponseEntity<List<ExamQuestionDTO>> getQuestions(@PathVariable Long examId) {
        return ResponseEntity.ok(ExamMapper.toQuestionsListDTO(questionService.getByExam(examId)));
    }

    @GetMapping("/questions/test/{attemptId}")
    public ResponseEntity<List<ExamTestQuestionDTO>> getTestQuestions(@PathVariable Long attemptId, Authentication authentication) {
        return ResponseEntity.ok(questionService.getTestQuestions(attemptId, authentication.getName()));
    }

    @DeleteMapping("/question/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join")
    public ResponseEntity<ExamResponseDTO> join(@RequestBody JoinExamDTO dto, Authentication authentication) {
        return ResponseEntity.ok(examService.join(authentication.getName(), dto.code()));
    }

    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<ExamResultDTO> submitExam(@PathVariable Long attemptId, @RequestBody List<UserAnswersDTO> answersDTO, Authentication authentication) {
        ExamResultDTO result = examService.submitExam(attemptId, authentication.getName(), answersDTO);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ExamHistoryDTO>> getHistory(Authentication authentication) {
        return ResponseEntity.ok(examService.getExamHistory(authentication.getName()));
    }

    @GetMapping("/created")
    public ResponseEntity<List<ExamDTO>> getMyExams(Authentication authentication) {
        return ResponseEntity.ok(examService.getMyExams(authentication.getName()));
    }

    @GetMapping("/{examId}/results")
    public ResponseEntity<List<ExamParticipantDTO>> getExamResults(@PathVariable Long examId, Authentication authentication) {
        return ResponseEntity.ok(examService.getExamResults(examId));
    }

    @GetMapping("/powers")
    public ResponseEntity<List<PowerUpDTO>> getPowerUps(Authentication authentication) {
        return ResponseEntity.ok(powerUpService.getPowers(authentication.getName()));
    }

    @PostMapping("/buy/{powerId}")
    public ResponseEntity<List<PowerUpDTO>> buyPowerUp(@PathVariable Long powerId,Authentication authentication) {
        powerUpService.buyPower(powerId, authentication.getName());

        return ResponseEntity.ok(powerUpService.getPowers(authentication.getName()));
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId, Authentication authentication) {
        examService.deleteExam(examId, authentication.getName());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/attempt/{attemptId}/use/{powerId}")
    public ResponseEntity<List<PowerUpDTO>> usePower(@PathVariable Long attemptId, @PathVariable Long powerId, Authentication authentication) {
        examService.usePower(attemptId, powerId, authentication.getName());

        return ResponseEntity.ok(powerUpService.getPowers(authentication.getName()));
    }

    @GetMapping("/attempt/{attemptId}")
    public ResponseEntity<AttemptPowerUsageDTO> getAttemptUsedPowers(@PathVariable Long attemptId, Authentication authentication) {
        return ResponseEntity.ok(examService.getUsedPowersInAttempt(attemptId, authentication.getName()));
    }

    @DeleteMapping("/{examId}/attempts/{attemptId}")
    public ResponseEntity<Void> deleteAttempt(@PathVariable Long examId, @PathVariable Long attemptId, Authentication authentication) {
        examService.deleteAttempt(examId, attemptId, authentication.getName());

        return ResponseEntity.noContent().build();
    }
}