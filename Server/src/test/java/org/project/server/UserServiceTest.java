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
        UserRegisterDTO dto = new UserRegisterDTO("username", "password123");

        User saved = userService.register(dto);

        assertNotNull(saved.getId());
        assertEquals("username", saved.getUsername());
        assertEquals(0, saved.getPoints());
        assertNotNull(saved.getFriends());
        assertTrue(saved.getFriends().isEmpty());

        System.out.println("Test user saved with id: " + saved.getId());

        userService.deleteUser(saved.getId());
    }

    @Test
    @Transactional
    void shouldAddFriend() {
        try {
            userService.addFriend("test","friend");

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
}
