package org.project.server.repository;

import org.project.server.model.Exam;
import org.project.server.model.ExamAttempt;
import org.project.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
    Optional<ExamAttempt> findByUserAndExam(User user, Exam exam);
    List<ExamAttempt> findByUserUsernameAndFinishedOrderByFinishedAtDesc(String username, boolean finished);
    List<ExamAttempt> findByExamIdAndFinishedTrueOrderByPercentageDesc(Long examId);
    Optional<ExamAttempt> findByIdAndUser(Long id, User user);
}
