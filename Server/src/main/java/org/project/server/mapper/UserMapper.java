package org.project.server.mapper;

import org.project.server.dto.*;
import org.project.server.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    private UserMapper() {
    }

    public static User toEntity(UserRegisterDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        return user;
    }

    public static UserResponseDTO toDTO(User user, List<AchievementDTO> achievements) {
        return new UserResponseDTO(user.getUsername(), user.getPoints(), achievements, allFriendsToDTO(user.getFriends()), CharacterMapper.toDTO(user.getCharacter()), user.getLoginStreak(), user.getSkin().getUrl());
    }

    public static UserRegisterDTO registerToDTO(User user) {
        return new UserRegisterDTO(user.getUsername(), user.getPassword());
    }

    public static UserProgressDTO progressToDTO(User user){
        return new UserProgressDTO(user.getPoints());
    }

    public static FriendDTO friendToDTO(User user){
        return new FriendDTO(user.getUsername(), CharacterMapper.toDTO(user.getCharacter()), user.getSkin().getUrl());
    }

    public static List<FriendDTO> allFriendsToDTO(List<User> friends){
        List<FriendDTO> friendsDTO = new ArrayList<>();
        for(User friend : friends) {
            friendsDTO.add(friendToDTO(friend));
        }

        return friendsDTO;
    }

    public static AuthResponseDTO authToDTO(String token, User user, List<AchievementDTO> achievements) {
        return new AuthResponseDTO(token, user.getUsername(), user.getPoints(), achievements, allFriendsToDTO(user.getFriends()), CharacterMapper.toDTO(user.getCharacter()), user.getLoginStreak(), user.getSkin().getUrl());
    }
}