package org.project.server.dto;

public record TaskRequestDTO(String name, Integer points, Long courseId, Boolean isDone) {
}
