package org.project.server.service;

import org.project.server.dto.ExamRequestDTO;
import org.project.server.mapper.ExamMapper;
import org.project.server.model.Exam;
import org.project.server.model.User;
import org.project.server.repository.ExamRepository;
import org.project.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final UserRepository userRepository;

    public ExamService(ExamRepository examRepository,
                       UserRepository userRepository) {
        this.examRepository = examRepository;
        this.userRepository = userRepository;
    }

    public Exam createExam(ExamRequestDTO dto, Long userId) {
        Exam exam = ExamMapper.toEntity(dto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        exam.setUser(user);

        return examRepository.save(exam);
    }

    public List<Exam> getAll() {
        return examRepository.findAll();
    }

    public Exam getById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    public Exam update(Long id, ExamRequestDTO dto) {
        Exam exam = getById(id);

        exam.setName(dto.getName());
        exam.setType(dto.getType());
        exam.setPoints(dto.getPoints());
        exam.setGrade(dto.getGrade());

        return examRepository.save(exam);
    }

    public void delete(Long id) {
        if (!examRepository.existsById(id)) {
            throw new RuntimeException("Exam not found");
        }
        examRepository.deleteById(id);
    }
}
