package org.project.server.service;

import org.project.server.dto.CourseProgressDTO;
import org.project.server.dto.CourseRequestDTO;
import org.project.server.mapper.CourseMapper;
import org.project.server.model.Course;
import org.project.server.model.User;
import org.project.server.repository.CourseRepository;
import org.project.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseService(UserRepository userRepository,
                         CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public Course createCourse(CourseRequestDTO dto, String username) {

        Course course = CourseMapper.toEntity(dto);

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Course not found"));

        course.setUser(user);
        course.setLevel(1);
        course.setExperience(0);

        return courseRepository.save(course);
    }

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Course> getMyCourses(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return courseRepository.findByUserId(user.getId());
    }

    public Course updateCourse(Long id, CourseRequestDTO dto) {

        Course course = getById(id);

        course.setName(dto.name());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    public Course updateProgress(Long courseId, CourseProgressDTO dto) {
        Course course = getById(courseId);

        course.setLevel(dto.getLevel());
        course.setExperience(dto.getExperience());

        return courseRepository.save(course);
    }

    public CourseProgressDTO addExperience(Course course, int points) {
        CourseProgressDTO dto = new CourseProgressDTO();
        dto.setLevel(course.getLevel());
        dto.setExperience(course.getExperience() + points);

        return dto;
    }

    public CourseProgressDTO removeExperience(Course course, int points) {
        CourseProgressDTO dto = new CourseProgressDTO();
        dto.setLevel(course.getLevel());
        dto.setExperience(Math.max(0, course.getExperience() - points));

        return dto;
    }
}
