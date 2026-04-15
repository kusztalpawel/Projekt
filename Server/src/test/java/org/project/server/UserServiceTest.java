package org.project.server;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.project.server.dto.UserRegisterDTO;
import org.project.server.model.Achievement;
import org.project.server.model.User;
import org.project.server.repository.AchievementRepository;
import org.project.server.repository.UserRepository;
import org.project.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AchievementRepository achievementRepository;

    @Test
    void createUser() {
        UserRegisterDTO dto = new UserRegisterDTO();
        dto.setUsername("username");
        dto.setPassword("password123");

        User saved = userService.register(dto);

        assertNotNull(saved.getId());
        assertEquals("username", saved.getUsername());
        assertEquals(1, saved.getLevel());
        assertEquals(0, saved.getExperience());

        System.out.println("Test user saved with id: " + saved.getId());

        userService.deleteUser(saved.getId());
    }

    @Test
    @Transactional
    void shouldAddFriend() {
        try {
            userService.addFriend(1L, 2L);

            User user1 = userRepository.findById(1L).orElseThrow();

            assertTrue(user1.getFriends().stream().anyMatch(u -> u.getId().equals(2L)));
        }
        finally {
            User u1 = userRepository.findById(1L).orElseThrow();
            User u2 = userRepository.findById(2L).orElseThrow();

            u1.getFriends().removeIf(u -> u.getId().equals(2L));
            u2.getFriends().removeIf(u -> u.getId().equals(1L));

            userRepository.save(u1);
            userRepository.save(u2);
        }
    }

    @Test
    @Transactional
    void shouldAssignAchievementToUser() {

        Achievement achievement = null;

        try {
            achievement = new Achievement();
            achievement.setName("First Login");
            achievement.setPoints(100);
            achievement = achievementRepository.save(achievement);

            userService.addAchievement(1L, achievement.getId());

            User user = userRepository.findById(1L).orElseThrow();

            Achievement finalAchievement = achievement;
            assertTrue(
                    user.getAchievements()
                            .stream()
                            .anyMatch(a -> a.getId().equals(finalAchievement.getId()))
            );
        }
        finally {
            if (achievement != null && achievement.getId() != null) {

                User user = userRepository.findById(1L).orElseThrow();

                Achievement finalAchievement1 = achievement;
                user.getAchievements()
                        .removeIf(a -> a.getId().equals(finalAchievement1.getId()));

                userRepository.save(user);

                achievementRepository.deleteById(achievement.getId());
            }
        }
    }
}
