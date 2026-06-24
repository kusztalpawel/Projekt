package org.project.server.controller;

import org.project.server.dto.AchievementDTO;
import org.project.server.dto.AchievementRequestDTO;
import org.project.server.mapper.AchievementMapper;
import org.project.server.model.Achievement;
import org.project.server.repository.UserRepository;
import org.project.server.service.AchievementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    private final AchievementService achievementService;
    private final UserRepository userRepository;

    public AchievementController(AchievementService achievementService, UserRepository userRepository) {
        this.achievementService = achievementService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<AchievementDTO> createAchievement(@RequestBody AchievementRequestDTO dto) {
        Achievement achievement = achievementService.createAchievement(AchievementMapper.toEntity(dto));

        return ResponseEntity.status(HttpStatus.CREATED).body(AchievementMapper.toDTO(achievement));
    }

    @GetMapping
    public ResponseEntity<List<AchievementDTO>> getAllUserAchievements(Authentication authentication) {
        return ResponseEntity.ok(AchievementMapper.toDTOList(achievementService.getUserAchievements(userRepository.findByUsername(authentication.getName()).orElseThrow())));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AchievementDTO>> getAllAchievements(Authentication authentication) {
        return ResponseEntity.ok(AchievementMapper.achievementsToDTOList(achievementService.getAllAchievements()));
    }
}
