package org.project.server.controller;

import org.project.server.dto.CharacterDTO;
import org.project.server.mapper.CharacterMapper;
import org.project.server.model.Attack;
import org.project.server.model.StatType;
import org.project.server.service.CharacterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/character")
public class CharacterController {
    CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @PostMapping("/upgrade/{stat}")
    public ResponseEntity<CharacterDTO> upgradeStat(Authentication authentication, @PathVariable StatType stat) {
        return ResponseEntity.ok(CharacterMapper.toDTO(characterService.addStatPoint(authentication.getName(), stat)));
    }

    @PostMapping("/fight/{friendUsername}")
    public ResponseEntity<List<Attack>> duelFriend(Authentication authentication, @PathVariable String friendUsername){
        return ResponseEntity.ok(characterService.duel(authentication.getName(), friendUsername));
    }
}
