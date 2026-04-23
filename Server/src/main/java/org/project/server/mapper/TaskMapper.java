package org.project.server.mapper;

import org.project.server.dto.TaskRequestDTO;
import org.project.server.dto.TaskResponseDTO;
import org.project.server.model.Task;

public class TaskMapper {
    private TaskMapper() {}


    public static Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setTime(dto.getTime());
        task.setPoints(dto.getPoints());
        return task;
    }

    public static TaskResponseDTO toDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setName(task.getName());
        dto.setTime(task.getTime());
        dto.setPoints(task.getPoints());
        dto.setUserId(task.getUser().getId());
        return dto;
    }
}
