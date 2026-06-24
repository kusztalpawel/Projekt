package org.project.server.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.project.server.model.Achievement;
import org.project.server.model.AchievementMetric;
import org.project.server.model.User;
import org.project.server.model.UserAchievement;
import org.project.server.repository.AchievementRepository;
import org.project.server.repository.UserAchievementRepository;
import org.project.server.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AchievementService {
    private final UserRepository userRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;

    public AchievementService(UserRepository userRepository, UserAchievementRepository userAchievementRepository, AchievementRepository achievementRepository) {
        this.userRepository = userRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.achievementRepository = achievementRepository;
    }

    public Achievement createAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    private boolean isAlreadyUnlocked(Long userId, Achievement achievement) {
        return userAchievementRepository.existsByUserIdAndAchievementId(userId, achievement.getId());
    }

    @Transactional
    public void unlockAchievement(Long userId, Achievement achievement) {
        if(!isAlreadyUnlocked(userId, achievement)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

            UserAchievement userAchievement = new UserAchievement();

            userAchievement.setUser(user);
            userAchievement.setAchievement(achievement);
            userAchievement.setUnlockedAt(LocalDateTime.now());
            userAchievementRepository.save(userAchievement);
        }
    }

    public List<UserAchievement> getUserAchievements(User user) {
        return userAchievementRepository.findAllByUserId(user.getId());
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @Transactional
    public void tryAchievementsUnlock(Long userId, AchievementMetric metric, int currentValue) {
        List<Achievement> achievements = achievementRepository.findAllByMetric(metric);

        for (Achievement achievement : achievements) {
            if (currentValue >= achievement.getRequirement()) {
                unlockAchievement(userId, achievement);
            }
        }
    }
}
