package org.project.server;

import org.junit.jupiter.api.Test;
import org.project.server.model.User;
import org.project.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void createUser() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password123");
        user.setLevel(1);
        user.setExperience(10);

        User saved = userService.saveUser(user);
        assertNotNull(saved.getId());

        System.out.println("Test user saved with id: " + saved.getId());

        userService.deleteUser(saved.getId());
    }
}
