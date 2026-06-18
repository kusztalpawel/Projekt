package org.project.server.dto;

import java.time.LocalDateTime;

public record AchievementDTO(String name, String description, LocalDateTime unlockedAt) {
}
