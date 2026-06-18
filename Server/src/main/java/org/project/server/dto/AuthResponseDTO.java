package org.project.server.dto;

import java.util.List;

public record AuthResponseDTO (String token, String username, Integer points, List<AchievementDTO> achievements, List<FriendDTO> friends, CharacterDTO character){
}