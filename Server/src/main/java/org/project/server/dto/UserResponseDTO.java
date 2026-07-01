package org.project.server.dto;

import java.util.List;

public record UserResponseDTO(String username, Integer points, List<AchievementDTO> achievements, List<FriendDTO> friends, CharacterDTO character, int loginStreak, String skinUrl, Integer powerCoins) {
}