package org.project.server.mapper;

import org.project.server.dto.AchievementDTO;
import org.project.server.model.Achievement;

public class AchievementMapper {
    public static AchievementDTO toDTO(Achievement a) {
        AchievementDTO dto = new AchievementDTO();
        dto.setId(a.getId());
        dto.setName(a.getName());
        dto.setPoints(a.getPoints());
        return dto;
    }
}
