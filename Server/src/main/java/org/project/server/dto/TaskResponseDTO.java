package org.project.server.dto;

import java.time.LocalDate;

public record TaskResponseDTO(Long id, String name, LocalDate date, Integer points, Long courseId, Boolean isDone){
}
