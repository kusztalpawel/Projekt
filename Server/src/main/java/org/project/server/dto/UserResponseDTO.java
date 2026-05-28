package org.project.server.dto;

import org.project.server.model.Achievement;

import java.util.List;

public record UserResponseDTO(String username, Integer points, List<Achievement> achievements, List<Long> friendsIds) {
}