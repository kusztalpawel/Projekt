package org.project.server.repository;

import org.project.server.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Long> {
    boolean  existsByUserIdAndAchievementId(Long userId, Long achievementId);
    List<UserAchievement> findAllByUserId(Long userId);
}
