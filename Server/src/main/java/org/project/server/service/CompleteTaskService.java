package org.project.server.service;

import jakarta.transaction.Transactional;
import org.project.server.model.Course;
import org.project.server.model.Task;
import org.project.server.model.User;
import org.springframework.stereotype.Service;

@Service
public class CompleteTaskService {
    private final TaskService taskService;
    private final CourseService courseService;
    private final UserService userService;

    public CompleteTaskService(TaskService taskService, CourseService courseService, UserService userService) {
        this.taskService = taskService;
        this.courseService = courseService;
        this.userService = userService;
    }

    @Transactional
    public Task completeTask(Long taskId, String username) {
        Task task = taskService.setTaskDone(taskId, username);
        Course course = task.getCourse();
        int newLevels;
        User user = course.getUser();

        if (Boolean.TRUE.equals(task.getDone())) {
            newLevels = courseService.setExperience(course, task.getPoints());
        } else {
            newLevels = courseService.setExperience(course, -task.getPoints());
        }

        userService.setPoints(user, newLevels);

        return task;
    }
}
