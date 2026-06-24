package org.project.server.controller;

import org.project.server.JwtUtil;
import org.project.server.dto.*;
import org.project.server.mapper.AchievementMapper;
import org.project.server.mapper.SkinMapper;
import org.project.server.mapper.UserMapper;
import org.project.server.model.Skin;
import org.project.server.model.User;
import org.project.server.service.AchievementService;
import org.project.server.service.CharacterService;
import org.project.server.service.SkinService;
import org.project.server.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final CharacterService characterService;
    private final AchievementService achievementService;
    private final SkinService skinService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, CharacterService characterService, JwtUtil jwtUtil, AchievementService achievementService, SkinService skinService) {
        this.userService = userService;
        this.characterService = characterService;
        this.achievementService = achievementService;
        this.jwtUtil = jwtUtil;
        this.skinService = skinService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDTO> register(@RequestBody UserRegisterDTO dto) {
        User user = userService.register(dto);
        characterService.createDefaultCharacter(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.registerToDTO(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody UserLoginDTO dto) {
        User user = userService.login(dto);
        List<AchievementDTO> achievementDTOs = AchievementMapper.toDTOList(achievementService.getUserAchievements(user));
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(UserMapper.authToDTO(token, user, achievementDTOs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        List<AchievementDTO> achievementDTOs = AchievementMapper.toDTOList(achievementService.getUserAchievements(user));
        return ResponseEntity.ok(UserMapper.toDTO(user, achievementDTOs));
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

    @GetMapping("/leaderboard")
    public ResponseEntity<List<UserRankingDTO>> getLeaderboard() {
        return ResponseEntity.ok(userService.getLeaderboard());
    }

    @PostMapping("/skin/{skinUrl}")
    public ResponseEntity<SkinDTO> getALlSkins(Authentication authentication,  @PathVariable String skinUrl) {
        User user = userService.getByUsername(authentication.getName());
        Skin newSkin = skinService.getByUrl(skinUrl);
        userService.setNewSkin(user, newSkin);
        return ResponseEntity.ok(SkinMapper.toDTO(newSkin, true));
    }

    @GetMapping("/skins")
    public ResponseEntity<List<SkinDTO>> getALlSkins(Authentication authentication) {
        User user = userService.getByUsername(authentication.getName());
        List<SkinDTO> skinDTOs = new ArrayList<>();
        for(Skin skin : skinService.getAllSkins()){
            skinDTOs.add(SkinMapper.toDTO(skin, skinService.isUnlocked(user, skin)));
        }

        return ResponseEntity.ok(skinDTOs);
    }
}
