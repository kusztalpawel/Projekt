package org.project.server.service;

import jakarta.transaction.Transactional;
import org.project.server.model.*;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CompleteTaskService {
    private final TaskService taskService;
    private final CourseService courseService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public CompleteTaskService(TaskService taskService, CourseService courseService, UserService userService, ApplicationEventPublisher publisher) {
        this.taskService = taskService;
        this.courseService = courseService;
        this.userService = userService;
        this.publisher = publisher;
    }

    @Transactional
    public Task completeTask(Long taskId, String username) {
        Task task = taskService.setTaskDone(taskId, username);
        Course course = task.getCourse();
        int newLevels;
        User user = course.getUser();

        userService.incrementStat(user, AchievementMetric.TASKS_COMPLETED);
        publisher.publishEvent(new ProgressEvent(user.getId(), AchievementMetric.TASKS_COMPLETED, user.getTasksCompleted()));

        if (Boolean.TRUE.equals(task.getDone())) {
            newLevels = courseService.setExperience(course, task.getPoints(), user);
        } else {
            newLevels = courseService.setExperience(course, -task.getPoints(), user);
        }

        userService.setPoints(user, newLevels);

        return task;
    }
}
