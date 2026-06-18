package org.project.server.dto;

import org.project.server.model.AchievementMetric;

public record AchievementRequestDTO(String name, String description, String code, AchievementMetric metric, Integer requirement) {
}
