package org.project.server.service;

import org.project.server.dto.*;
import org.project.server.model.*;
import org.project.server.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final UserRepository userRepository;
    private final ExamAttemptRepository examAttemptRepository;
    private final PowerUpRepository powerUpRepository;
    private final UserPowerRepository userPowerRepository;

    public ExamService(ExamRepository examRepository, ExamQuestionRepository examQuestionRepository, UserRepository userRepository, ExamAttemptRepository examAttemptRepository, PowerUpRepository powerUpRepository, UserPowerRepository userPowerRepository) {
        this.examRepository = examRepository;
        this.examQuestionRepository = examQuestionRepository;
        this.userRepository = userRepository;
        this.examAttemptRepository = examAttemptRepository;
        this.powerUpRepository = powerUpRepository;
        this.userPowerRepository = userPowerRepository;
    }

    public Exam create(ExamDTO dto, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> (new RuntimeException("User not found")));

        if (examRepository.existsByCode(dto.code())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exam code already exists");
        }

        Exam exam = new Exam();

        exam.setName(dto.name());
        exam.setCode(dto.code());
        exam.setTimeLimit(dto.timeLimit());
        exam.setCreatedBy(user);

        return examRepository.save(exam);
    }

    @Transactional
    public void deleteExam(Long examId, String username) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (!exam.getCreatedBy().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot delete this exam.");
        }

        examRepository.delete(exam);
    }

    public List<Exam> getAll() {
        return examRepository.findAll();
    }

    public Exam getById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found."));
    }

    public ExamResponseDTO join(String username, String code) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found."));
        Exam exam = examRepository.findByCode(code).orElseThrow();

        ExamAttempt examAttempt = examAttemptRepository.findByUserAndExam(user, exam).orElseGet(
                () -> {
                    ExamAttempt newAttempt = new ExamAttempt();
                    newAttempt.setUser(user);
                    newAttempt.setExam(exam);
                    newAttempt.setStartedAt(LocalDateTime.now());
                    newAttempt.setFinished(false);

                    return examAttemptRepository.save(newAttempt);
                }
        );

        if(examAttempt.isFinished()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exam already finished");
        }

        return new ExamResponseDTO(examAttempt.getId(), examAttempt.getStartedAt(), exam.getId(), exam.getName(), exam.getTimeLimit(), examAttempt.isBonusMinutesUsed(), examAttempt.isBonusPointsUsed(), examAttempt.isRemoveOptionUsed());
    }

    private double calculateGrade(double percentage) {
        if (percentage >= 90)
            return 5.0;
        if (percentage >= 80)
            return 4.5;
        if (percentage >= 70)
            return 4.0;
        if (percentage >= 60)
            return 3.5;
        if (percentage >= 50)
            return 3.0;

        return 2.0;
    }

    public ExamResultDTO submitExam(Long attemptId, String username, List<UserAnswersDTO> examAnswersDTO) {
        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId).orElseThrow(() -> new RuntimeException("Exam attempt not found."));

        if(!examAttempt.getUser().getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        if(examAttempt.isFinished()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Exam already finished");
        }

        List<ExamQuestion> questions = examQuestionRepository.findByExamId(examAttempt.getExam().getId());
        int correctAnswers = 0;
        int points = 0;
        int totalPoints = 0;

        LocalDateTime endTime = examAttempt.getStartedAt().plusMinutes(examAttempt.getExam().getTimeLimit());

        if(examAttempt.isBonusMinutesUsed()){
            PowerUp powerUp = powerUpRepository.findFirstByType(PowerUpType.BONUS_TIME).orElseThrow(() -> (new RuntimeException("Power up not found.")));
            endTime = endTime.plusMinutes(powerUp.getValue());
        }

        if(LocalDateTime.now().isAfter(endTime.plusSeconds(30))){
            examAttempt.setFinished(true);
            examAttempt.setFinishedAt(LocalDateTime.now());
            examAttempt.setCorrectAnswers(0);
            examAttempt.setTotalQuestions(questions.size());
            examAttempt.setPoints(0);
            examAttempt.setTotalPoints(totalPoints);
            examAttempt.setPercentage(0);
            examAttempt.setGrade(2);

            examAttemptRepository.save(examAttempt);

            return new ExamResultDTO(0, questions.size(), 0, totalPoints, 0, 2);
        }

        for (UserAnswersDTO answer : examAnswersDTO) {
            ExamQuestion question = questions.stream().filter(q->q.getId().equals(answer.questionId())).findFirst().orElseThrow(() -> new RuntimeException("Question not found"));

            ExamAnswer correctAnswer = question.getAnswers()
                            .stream()
                            .filter(ExamAnswer::getCorrect)
                            .findFirst()
                            .orElseThrow();

            if (correctAnswer.getId().equals(answer.answerId())) {
                correctAnswers++;
                points += question.getPoints();
            }
        }

        for(ExamQuestion question : questions){
            totalPoints += question.getPoints();
        }

        if(examAttempt.isBonusPointsUsed()){
            PowerUp powerUp = powerUpRepository.findFirstByType(PowerUpType.BONUS_POINTS).orElseThrow(() -> new RuntimeException("Bonus points not found."));
            points += powerUp.getValue();
            if(points >= totalPoints){
                points = totalPoints;
            }
        }

        double percentage = totalPoints == 0 ? 0 : (points * 100.0) / totalPoints;

        examAttempt.setFinished(true);
        examAttempt.setFinishedAt(LocalDateTime.now());
        examAttempt.setCorrectAnswers(correctAnswers);
        examAttempt.setTotalQuestions(questions.size());
        examAttempt.setPoints(points);
        examAttempt.setTotalPoints(totalPoints);
        examAttempt.setPercentage(percentage);
        examAttempt.setGrade(calculateGrade(percentage));

        examAttemptRepository.save(examAttempt);

        return new ExamResultDTO(correctAnswers, questions.size(), points, totalPoints, percentage, calculateGrade(percentage));
    }

    public List<ExamHistoryDTO> getExamHistory(String username) {
        List<ExamAttempt> attempts = examAttemptRepository.findByUserUsernameAndFinishedOrderByFinishedAtDesc(username, true);

        return attempts.stream().map(attempt -> new ExamHistoryDTO(
                        attempt.getId(),
                        attempt.getExam().getName(),
                        attempt.getCorrectAnswers(),
                        attempt.getTotalQuestions(),
                        attempt.getPoints(),
                        attempt.getTotalPoints(),
                        attempt.getPercentage(),
                        attempt.getGrade(),
                        attempt.getFinishedAt())).toList();
    }

    public List<ExamDTO> getMyExams(String username) {
        List<Exam> exams = examRepository.findByCreatedByUsernameOrderByIdDesc(username);
        return exams.stream().map(exam -> new ExamDTO(exam.getId(), exam.getName(), exam.getCode(), exam.getTimeLimit())).toList();
    }

    public List<ExamParticipantDTO> getExamResults(Long examId) {

        List<ExamAttempt> attempts = examAttemptRepository.findByExamIdAndFinishedTrueOrderByPercentageDesc(examId);

        return attempts.stream().map(attempt -> new ExamParticipantDTO(
                attempt.getUser().getUsername(),
                new ExamHistoryDTO(attempt.getId(), attempt.getExam().getName(), attempt.getCorrectAnswers(), attempt.getTotalQuestions(), attempt.getPoints(), attempt.getTotalPoints(), attempt.getPercentage(), attempt.getGrade(), attempt.getFinishedAt())
        )).toList();
    }

    @Transactional
    public void usePower(Long attemptId, Long powerId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ExamAttempt attempt = examAttemptRepository.findByIdAndUser(attemptId, user).orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (attempt.isFinished()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exam already finished.");
        }

        PowerUp power = powerUpRepository.findById(powerId).orElseThrow(() -> new RuntimeException("Power not found"));

        UserPower userPower = userPowerRepository.findByUserUsernameAndPowerUpId(username, powerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Power not owned"));

        if (userPower.getAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Power not owned");
        }

        switch (power.getType()) {
            case BONUS_POINTS -> {
                if (attempt.isBonusPointsUsed()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already used.");
                }
                attempt.setBonusPointsUsed(true);
            }
            case BONUS_TIME -> {
                if (attempt.isBonusMinutesUsed()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already used.");
                }
                attempt.setBonusMinutesUsed(true);
            }

            case REMOVE_OPTION -> {
                if (attempt.isRemoveOptionUsed()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already used.");
                }
                attempt.setRemoveOptionUsed(true);
            }
        }

        userPower.setAmount(userPower.getAmount() - 1);

        examAttemptRepository.save(attempt);
        userPowerRepository.save(userPower);
    }

    public AttemptPowerUsageDTO getUsedPowersInAttempt(Long attemptId, String username) {
        ExamAttempt examAttempt = examAttemptRepository.findById(attemptId).orElseThrow(() -> new RuntimeException("Attempt not found"));

        if(!examAttempt.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Attempt not owned.");
        }

        return new AttemptPowerUsageDTO(examAttempt.isBonusMinutesUsed(), examAttempt.isBonusPointsUsed(), examAttempt.isRemoveOptionUsed());
    }

    @Transactional
    public void deleteAttempt(Long examId, Long attemptId, String username) {

        Exam exam = examRepository.findById(examId).orElseThrow(() -> new RuntimeException("Exam not found"));

        if (!exam.getCreatedBy().getUsername().equals(username))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not authorized to perform this action.");

        ExamAttempt attempt = examAttemptRepository.findById(attemptId).orElseThrow(() -> new RuntimeException("Attempt not found"));

        if (!attempt.getExam().getId().equals(examId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Attempt does not belong to this exam");

        examAttemptRepository.delete(attempt);
    }
}
