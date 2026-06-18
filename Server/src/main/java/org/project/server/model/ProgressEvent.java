package org.project.server.model;

public record ProgressEvent(Long userId, AchievementMetric metric, int currentValue) {
}
