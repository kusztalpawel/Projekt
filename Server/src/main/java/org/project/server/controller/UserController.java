package org.project.server.controller;

import org.project.server.JwtUtil;
import org.project.server.dto.*;
import org.project.server.mapper.UserMapper;
import org.project.server.model.User;
import org.project.server.service.CharacterService;
import org.project.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CharacterService characterService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, CharacterService characterService,  JwtUtil jwtUtil) {
        this.userService = userService;
        this.characterService = characterService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRegisterDTO dto) {
        User user = userService.register(dto);
        characterService.createDefaultCharacter(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UserMapper.toDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        User user = userService.login(dto);
        String token = jwtUtil.generateToken(user.getUsername());

        return ResponseEntity.ok(UserMapper.authToDTO(token, user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(UserMapper.toDTO(userService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/progress")
    public ResponseEntity<UserProgressDTO> updateProgress(Authentication authentication) {
        return ResponseEntity.ok(UserMapper.progressToDTO(userService.getByUsername(authentication.getName())));
    }

    @PostMapping("/friend/{friendUsername}")
    public ResponseEntity<List<FriendDTO>> addFriend(Authentication authentication, @PathVariable String friendUsername) {
        String username = authentication.getName();
        userService.addFriend(username, friendUsername);
        return ResponseEntity.ok(UserMapper.allFriendsToDTO((userService.getFriends(username))));
    }

    @GetMapping("/friend")
    public ResponseEntity<List<FriendDTO>> getFriends(Authentication authentication) {
        return ResponseEntity.ok(UserMapper.allFriendsToDTO((userService.getFriends(authentication.getName()))));
    }
}
