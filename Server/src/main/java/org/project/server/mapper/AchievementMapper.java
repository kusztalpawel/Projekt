package org.project.server.mapper;

import org.project.server.dto.AchievementDTO;
import org.project.server.dto.AchievementRequestDTO;
import org.project.server.model.Achievement;
import org.project.server.model.UserAchievement;

import java.util.List;

public class AchievementMapper {
    private AchievementMapper() {}

    public static AchievementDTO toDTO(UserAchievement userAchievement) {
        return new AchievementDTO(userAchievement.getAchievement().getName(), userAchievement.getAchievement().getDescription(), userAchievement.getUnlockedAt());
    }

    public static AchievementDTO toDTO(Achievement achievement) {
        return new AchievementDTO(achievement.getName(), achievement.getDescription(), null);
    }

    public static List<AchievementDTO> toDTOList(List<UserAchievement> userAchievements) {
        return userAchievements.stream()
                .map(AchievementMapper::toDTO)
                .toList();
    }

    public static List<AchievementDTO> achievementsToDTOList(List<Achievement> achievements) {
        return achievements.stream()
                .map(AchievementMapper::toDTO)
                .toList();
    }

    public static Achievement toEntity(AchievementRequestDTO dto) {
        Achievement achievement = new Achievement();

        achievement.setName(dto.name());
        achievement.setDescription(dto.description());
        achievement.setMetric(dto.metric());
        achievement.setRequirement(dto.requirement());
        achievement.setCode(dto.code());

        return achievement;
    }
}
