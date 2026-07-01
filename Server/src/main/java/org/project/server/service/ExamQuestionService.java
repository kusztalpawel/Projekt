package org.project.server.service;

import org.project.server.dto.ExamAnswerDTO;
import org.project.server.dto.ExamQuestionDTO;
import org.project.server.dto.ExamTestQuestionDTO;
import org.project.server.model.*;
import org.project.server.repository.ExamAttemptRepository;
import org.project.server.repository.ExamQuestionRepository;
import org.project.server.repository.ExamRepository;
import org.project.server.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExamQuestionService {

    private final ExamQuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final UserRepository userRepository;

    public ExamQuestionService(
            ExamQuestionRepository questionRepository,
            ExamRepository examRepository, ExamAttemptRepository examAttemptRepository, UserRepository userRepository) {

        this.questionRepository = questionRepository;
        this.examRepository = examRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.userRepository = userRepository;
    }

    public ExamQuestion create(ExamQuestionDTO dto) {

        Exam exam = examRepository.findById(dto.examId()).orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamQuestion question = new ExamQuestion();

        question.setExam(exam);
        question.setText(dto.text());
        question.setPoints(dto.points());

        if (dto.answers().size() != 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Each question must have exactly 4 answers.");
        }

        for (ExamAnswerDTO answerDTO : dto.answers()) {

            ExamAnswer answer = new ExamAnswer();

            answer.setText(answerDTO.text());
            answer.setCorrect(answerDTO.correct());

            question.addAnswer(answer);
        }

        return questionRepository.save(question);
    }

    public List<ExamTestQuestionDTO> getTestQuestions(Long attemptId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ExamAttempt attempt = examAttemptRepository.findByIdAndUser(attemptId, user).orElseThrow(() -> new RuntimeException("Attempt not found"));

        List<ExamQuestion> questions = questionRepository.findByExamId(attempt.getExam().getId());

        return questions.stream().map(question -> {
            List<ExamAnswer> answers = new ArrayList<>(question.getAnswers());

            if (attempt.isRemoveOptionUsed()) {
                int indexToRemove = answers.size();
                boolean isCorrect = true;

                while(indexToRemove > 0 && isCorrect) {
                    indexToRemove--;
                    isCorrect = answers.get(indexToRemove).getCorrect();
                }

                answers.remove(indexToRemove);
            }

            Map<Long, String> answerMap = answers.stream().collect(Collectors.toMap(ExamAnswer::getId, ExamAnswer::getText));

            return new ExamTestQuestionDTO(question.getId(), attempt.getExam().getId(), question.getText(), question.getPoints(), answerMap);
        }).toList();
    }

    public List<ExamQuestion> getByExam(Long examId) {
        return questionRepository.findByExamId(examId);
    }

    public void delete(Long questionId) {
        if (!questionRepository.existsById(questionId)) {
            throw new RuntimeException("Question not found.");
        }

        questionRepository.deleteById(questionId);
    }
}
