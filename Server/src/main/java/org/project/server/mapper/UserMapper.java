package org.project.server.mapper;

import org.project.server.dto.UserRegisterDTO;
import org.project.server.dto.UserResponseDTO;
import org.project.server.model.User;

public class UserMapper {

    public static User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setLevel(1);
        user.setExperience(0);
        return user;
    }

    public static UserResponseDTO toDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setLevel(user.getLevel());
        dto.setExperience(user.getExperience());

        dto.setAchievements(
                user.getAchievements()
                        .stream()
                        .map(AchievementMapper::toDTO)
                        .toList()
        );

        return dto;
    }
}