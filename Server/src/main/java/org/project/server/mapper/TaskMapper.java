package org.project.server.mapper;

import org.project.server.dto.TaskRequestDTO;
import org.project.server.dto.TaskResponseDTO;
import org.project.server.model.Task;

public class TaskMapper {
    private TaskMapper() {}

    public static Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setName(dto.name());
        task.setPoints(dto.points());
        task.setDone(dto.isDone());
        return task;
    }

    public static TaskResponseDTO toDTO(Task task) {
        return new TaskResponseDTO(task.getId(), task.getName(), task.getTime(), task.getPoints(), task.getCourse().getId(), task.getDone());
    }
}
