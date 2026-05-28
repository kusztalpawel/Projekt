package org.project.server.mapper;

import org.project.server.dto.UserRegisterDTO;
import org.project.server.dto.UserResponseDTO;
import org.project.server.model.User;

public class UserMapper {
    private UserMapper() {
    }

    public static User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getUsername(), user.getPoints(), user.getAchievements(), user.getFriendsIds());
    }
}