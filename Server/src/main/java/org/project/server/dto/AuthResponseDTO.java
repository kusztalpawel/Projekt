package org.project.server.dto;

import org.project.server.model.Achievement;

import java.util.List;

public record AuthResponseDTO (String token, String username, Integer points, List<Achievement> achievements, List<FriendDTO> friends, CharacterDTO character){
}