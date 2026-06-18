package org.project.server;

import org.project.server.model.ProgressEvent;
import org.project.server.service.AchievementService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AchievementListener {
    private final AchievementService achievementService;

    public AchievementListener(
            AchievementService achievementService
    ) {
        this.achievementService = achievementService;
    }

    @EventListener
    public void onEventCalled(ProgressEvent event) {
        achievementService.tryAchievementsUnlock(event.userId(), event.metric(), event.currentValue());
    }
}
