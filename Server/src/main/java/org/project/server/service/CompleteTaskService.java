package org.project.server.service;

import jakarta.transaction.Transactional;
import org.project.server.dto.CourseProgressDTO;
import org.project.server.model.Course;
import org.project.server.model.Task;
import org.springframework.stereotype.Service;

@Service
public class CompleteTaskService {
    private final TaskService taskService;
    private final CourseService courseService;

    public CompleteTaskService(TaskService taskService, CourseService courseService) {
        this.taskService = taskService;
        this.courseService = courseService;
    }

    @Transactional
    public Task completeTask(Long taskId, String username) {
        Task task = taskService.setTaskDone(taskId, username);
        Course course = task.getCourse();
        CourseProgressDTO dto;

        if (Boolean.TRUE.equals(task.getDone())) {
            dto = courseService.addExperience(course, task.getPoints());
        } else {
            dto = courseService.removeExperience(course, task.getPoints());
        }

        courseService.updateProgress(course.getId(), dto);

        return task;
    }
}
