package org.project.server.service;

import org.project.server.dto.CourseRequestDTO;
import org.project.server.model.*;
import org.project.server.repository.CourseRepository;
import org.project.server.repository.TaskRepository;
import org.project.server.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher publisher;
    private final UserService userService;

    public CourseService(UserRepository userRepository, CourseRepository courseRepository, TaskRepository taskRepository, ApplicationEventPublisher publisher, UserService userService) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
        this.publisher = publisher;
        this.userService = userService;
    }

    public Course createCourse(Course course, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRole() == UserRole.ADMIN) {
            course.setUser(null);
        } else {
            course.setUser(user);
        }

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

    public int setExperience(Course course, int points, User user) {
        int oldLevel = course.getLevel();
        int level = oldLevel;
        int experience = course.getExperience() + points;

        int experienceNeeded = level * 50;

        while (experience >= experienceNeeded) {
            experience -= experienceNeeded;
            level++;
            userService.incrementStat(user, AchievementMetric.LEVELS_COMPLETED);
            publisher.publishEvent(new ProgressEvent(user.getId(), AchievementMetric.LEVELS_COMPLETED, user.getLevelsCompleted()));
            experienceNeeded = level * 50;
        }

        course.setLevel(level);
        course.setExperience(Math.max(experience, 0));

        return Math.max(level - oldLevel, 0);
    }

    public List<Course> getTemplates(User user) {
        List<Course> templates = courseRepository.findByUserIsNull();

        return templates.stream()
                .filter(template ->
                        !courseRepository.existsByUserAndName(user, template.getName()))
                .toList();
    }

    @Transactional
    public Course enroll(Long courseId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Course template = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        boolean exists = courseRepository.existsByUserAndName(user, template.getName());

        if (exists) {
            return courseRepository.findByUserAndName(user, template.getName());
        }

        Course newCourse = new Course();
        newCourse.setName(template.getName());
        newCourse.setUser(user);
        newCourse.setLevel(1);
        newCourse.setExperience(0);

        Course savedCourse = courseRepository.save(newCourse);

        List<Task> tasks = taskRepository.findByCourse(template);

        for (Task t : tasks) {
            Task copy = new Task();
            copy.setName(t.getName());
            copy.setPoints(t.getPoints());
            copy.setCourse(savedCourse);

            taskRepository.save(copy);
        }

        return savedCourse;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
