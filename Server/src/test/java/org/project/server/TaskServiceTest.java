package org.project.server;

import org.junit.jupiter.api.Test;
import org.project.server.dto.TaskRequestDTO;
import org.project.server.model.Task;
import org.project.server.repository.TaskRepository;
import org.project.server.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldCreateTaskForUser() {

        Task created = null;

        try {
            TaskRequestDTO dto = new TaskRequestDTO();
            dto.setName("Task 1");
            dto.setPoints(10);
            dto.setTime(LocalDate.now());

            created = taskService.createTask(dto, 1L);

            assertNotNull(created.getId());
            assertEquals(1L, created.getUser().getId());
        }
        finally {
            if (created != null && created.getId() != null) {
                taskRepository.deleteById(created.getId());
            }
        }
    }
}
