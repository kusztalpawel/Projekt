package org.project.server.service;

import org.project.server.dto.TaskRequestDTO;
import org.project.server.mapper.TaskMapper;
import org.project.server.model.Course;
import org.project.server.model.Task;
import org.project.server.repository.CourseRepository;
import org.project.server.repository.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;

    public TaskService(TaskRepository taskRepository, CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
    }

    public Task createTask(TaskRequestDTO dto, String username) {

        Task task = TaskMapper.toEntity(dto);

        Course course = courseRepository.findById(dto.courseId()).orElseThrow();

        if (!course.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        task.setTime(LocalDate.now());
        task.setCourse(course);
        task.setDone(false);

        return taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public List<Task> getUserTasksByCourseId(Long id, String name) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getUser().getUsername().equals(name)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return taskRepository.findByCourseId(id);
    }

    public Task updateTask(Long id, TaskRequestDTO dto, String username) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if(!task.getCourse().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        task.setName(dto.name());
        task.setTime(LocalDate.now());
        task.setPoints(dto.points());

        return taskRepository.save(task);
    }

    public Task setTaskDone(Long id, String username) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if(!task.getCourse().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        task.setDone(!task.getDone());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id, String username) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        if(!task.getCourse().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        taskRepository.deleteById(id);
    }
}
