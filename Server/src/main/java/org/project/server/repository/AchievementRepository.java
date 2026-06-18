package org.project.server.repository;

import org.project.server.model.Achievement;
import org.project.server.model.AchievementMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Optional<Achievement> findByCode(String code);
    List<Achievement> findAllByMetric(AchievementMetric metric);
}
