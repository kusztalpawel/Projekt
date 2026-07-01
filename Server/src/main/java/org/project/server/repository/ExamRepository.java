package org.project.server.repository;

import org.project.server.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>{
    Optional<Exam> findByCode(String code);
    boolean existsByCode(String code);
    List<Exam> findByCreatedByUsernameOrderByIdDesc(String username);
}
