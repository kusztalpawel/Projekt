package org.project.server.service;

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

    public int setExperience(Course course, int points) {
        int oldLevel = course.getLevel();
        int level = oldLevel;
        int experience = course.getExperience() + points;

        int experienceNeeded = level * 50;

        while (experience >= experienceNeeded) {
            experience -= experienceNeeded;
            level++;
            experienceNeeded = level * 50;
        }

        course.setLevel(level);
        course.setExperience(Math.max(experience, 0));

        return Math.max(level - oldLevel, 0);
    }
}
