package org.project.server.controller;

import org.project.server.dto.TaskRequestDTO;
import org.project.server.dto.TaskResponseDTO;
import org.project.server.mapper.TaskMapper;
import org.project.server.model.Task;
import org.project.server.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO dto, Authentication authentication) {

        String username = authentication.getName();

        Task task = taskService.createTask(dto, username);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TaskMapper.toDTO(task));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAll() {
        return ResponseEntity.ok(
                taskService.getAll()
                        .stream()
                        .map(TaskMapper::toDTO)
                        .toList()
        );
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<TaskResponseDTO>> getByCourseId(@PathVariable Long courseId, Authentication authentication) {
        String name = authentication.getName();

        return ResponseEntity.ok(taskService.getUserTasksByCourseId(courseId, name).stream().map(TaskMapper::toDTO).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                TaskMapper.toDTO(taskService.getById(id))
        );
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody TaskRequestDTO dto) {

        Task updated = taskService.updateTask(id, dto);

        return ResponseEntity.ok(TaskMapper.toDTO(updated));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();

        taskService.deleteTask(id, username);
        return ResponseEntity.noContent().build();
    }
}