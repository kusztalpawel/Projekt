package org.project.server.controller;

import org.project.server.dto.CompleteTaskResponseDTO;
import org.project.server.dto.TaskRequestDTO;
import org.project.server.dto.TaskResponseDTO;
import org.project.server.dto.UserProgressDTO;
import org.project.server.mapper.CourseMapper;
import org.project.server.mapper.TaskMapper;
import org.project.server.mapper.UserMapper;
import org.project.server.model.Course;
import org.project.server.model.Task;
import org.project.server.model.User;
import org.project.server.service.CompleteTaskService;
import org.project.server.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CompleteTaskService completeTaskService;

    public TaskController(TaskService taskService, CompleteTaskService completeTaskService) {
        this.taskService = taskService;
        this.completeTaskService = completeTaskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO dto, Authentication authentication) {

        String username = authentication.getName();

        Task task = taskService.createTask(dto, username);

        return ResponseEntity.status(HttpStatus.CREATED).body(TaskMapper.toDTO(task));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll() {
        return ResponseEntity.ok(taskService.getAll().stream().map(TaskMapper::toDTO).toList());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TaskResponseDTO>> getByCourseId(@PathVariable Long courseId, Authentication authentication) {
        String name = authentication.getName();

        return ResponseEntity.ok(taskService.getUserTasksByCourseId(courseId, name).stream().map(TaskMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(TaskMapper.toDTO(taskService.getById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @RequestBody TaskRequestDTO dto, Authentication authentication) {

        String username = authentication.getName();
        Task updated = taskService.updateTask(id, dto, username);

        return ResponseEntity.ok(TaskMapper.toDTO(updated));
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<CompleteTaskResponseDTO> setDone(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Task task = completeTaskService.completeTask(id, username);
        Course course = task.getCourse();

        CompleteTaskResponseDTO response = new CompleteTaskResponseDTO(TaskMapper.toDTO(task), CourseMapper.progressToDTO(course));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();

        taskService.deleteTask(id, username);
        return ResponseEntity.noContent().build();
    }
}