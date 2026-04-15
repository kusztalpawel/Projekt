package org.project.server.controller;

import org.project.server.dto.TaskRequestDTO;
import org.project.server.dto.TaskResponseDTO;
import org.project.server.mapper.TaskMapper;
import org.project.server.model.Task;
import org.project.server.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> create(@RequestBody TaskRequestDTO dto) {

        Long userId = 1L; // docelowo z JWT / security context

        Task task = taskService.createTask(dto, userId);

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

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                TaskMapper.toDTO(taskService.getById(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody TaskRequestDTO dto) {

        Task updated = taskService.updateTask(id, dto);

        return ResponseEntity.ok(TaskMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}