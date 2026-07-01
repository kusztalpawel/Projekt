package org.project.server.repository;

import org.project.server.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long>{
    List<ExamQuestion> findByExamId(Long examId);
}
