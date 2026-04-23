package org.project.server.controller;

import org.project.server.JwtUtil;
import org.project.server.dto.*;
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
    private final JwtUtil jwtUtil;

    public UserController(UserService userService,  JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO dto) {
        User user = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        User user = userService.login(dto);
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(new AuthResponseDTO(token));
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
