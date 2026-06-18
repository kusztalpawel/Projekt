package org.project.server.service;

import org.project.server.dto.UserLoginDTO;
import org.project.server.dto.UserRegisterDTO;
import org.project.server.mapper.UserMapper;
import org.project.server.model.AchievementMetric;
import org.project.server.model.ProgressEvent;
import org.project.server.model.User;
import org.project.server.repository.UserRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher publisher) {
        this.userRepository = userRepository;
        this.publisher = publisher;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(UserRegisterDTO dto) {
        if (userRepository.findByUsername(dto.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }

        User user = UserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPoints(0);
        return userRepository.save(user);
    }

    public User login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.username()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        publisher.publishEvent(new ProgressEvent(user.getId(), AchievementMetric.LOGIN_STREAK, user.getLoginStreak()));

        return user;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public void addFriend(String username, String friendUsername) {
        if (username.equals(friendUsername)) {
            throw new IllegalArgumentException("Cannot add yourself as friend");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        if (user.getFriends().contains(friend)) {
            return;
        }

        user.getFriends().add(friend);
        friend.getFriends().add(user);

        userRepository.save(user);
        userRepository.save(friend);

        incrementStat(user, AchievementMetric.FRIENDS_COUNT);
        publisher.publishEvent(new ProgressEvent(user.getId(), AchievementMetric.FRIENDS_COUNT, user.getFriendsCount()));
        incrementStat(friend, AchievementMetric.FRIENDS_COUNT);
        publisher.publishEvent(new ProgressEvent(friend.getId(), AchievementMetric.FRIENDS_COUNT, friend.getFriendsCount()));
    }

    public List<User> getFriends(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return user.getFriends();
    }

    public void setPoints(User user, int points) {
        user.setPoints(user.getPoints() + points);
    }

    public void incrementStat(User user, AchievementMetric achievementMetric) {
        switch(achievementMetric) {
            case FRIENDS_COUNT -> user.setFriendsCount(user.getFriendsCount() + 1);
            case TASKS_COMPLETED -> user.setTasksCompleted(user.getTasksCompleted() + 1);
            case LOGIN_STREAK-> user.setLoginStreak(user.getLoginStreak() + 1);
            case LEVELS_COMPLETED -> user.setLevelsCompleted(user.getLevelsCompleted() + 1);
            case SKILLPOINTS_USED -> user.setSkillpointsUsed(user.getSkillpointsUsed() + 1);
        };
    }
}
