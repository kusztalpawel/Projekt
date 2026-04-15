package org.project.server.service;

import org.project.server.controller.UserProgressDTO;
import org.project.server.dto.UserLoginDTO;
import org.project.server.dto.UserRegisterDTO;
import org.project.server.mapper.UserMapper;
import org.project.server.model.Achievement;
import org.project.server.model.User;
import org.project.server.repository.AchievementRepository;
import org.project.server.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AchievementRepository achievementRepository;

    public UserService(UserRepository userRepository, AchievementRepository achievementRepository) {
        this.userRepository = userRepository;
        this.achievementRepository = achievementRepository;
    }

    public User register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        User user = UserMapper.toEntity(dto);
        return userRepository.save(user);
    }

    public User login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User updateProgress(Long userId, UserProgressDTO dto) {
        User user = getById(userId);

        user.setLevel(dto.getLevel());
        user.setExperience(dto.getExperience());

        return userRepository.save(user);
    }

    public void addAchievement(Long userId, Long achievementId) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow();

        user.getAchievements().add(achievement);

        userRepository.save(user);
    }

    public void addFriend(Long userId, Long friendId) {

        User user = userRepository.findById(userId).orElseThrow();
        User friend = userRepository.findById(friendId).orElseThrow();

        user.getFriends().add(friend);

        userRepository.save(user);
    }
}
