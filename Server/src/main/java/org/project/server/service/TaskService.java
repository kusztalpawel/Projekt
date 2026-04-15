package org.project.server.service;

import org.project.server.dto.TaskRequestDTO;
import org.project.server.mapper.TaskMapper;
import org.project.server.model.Task;
import org.project.server.model.User;
import org.project.server.repository.TaskRepository;
import org.project.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(TaskRequestDTO dto, Long userId) {

        Task task = TaskMapper.toEntity(dto);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setUser(user);

        return taskRepository.save(task);
    }

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task updateTask(Long id, TaskRequestDTO dto) {

        Task task = getById(id);

        task.setName(dto.getName());
        task.setTime(dto.getTime());
        task.setPoints(dto.getPoints());

        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
