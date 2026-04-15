package org.project.server.controller;

import org.project.server.dto.UserLoginDTO;
import org.project.server.dto.UserRegisterDTO;
import org.project.server.dto.UserResponseDTO;
import org.project.server.mapper.UserMapper;
import org.project.server.model.User;
import org.project.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO dto) {
        User user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserLoginDTO dto) {
        User user = userService.login(dto);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.ok(UserMapper.toDTO(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<UserResponseDTO> updateProgress(
            @PathVariable Long id,
            @RequestBody UserProgressDTO dto) {

        User updated = userService.updateProgress(id, dto);
        return ResponseEntity.ok(UserMapper.toDTO(updated));
    }
}
