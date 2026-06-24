package org.project.server.service;

import org.project.server.model.Skin;
import org.project.server.model.User;
import org.project.server.repository.SkinRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkinService {
    private final SkinRepository skinRepository;
    private final UserService userService;

    public SkinService(SkinRepository skinRepository, UserService userService) {
        this.skinRepository = skinRepository;
        this.userService = userService;
    }

    public List<Skin> getAllSkins() {
        return skinRepository.findAll();
    }

    public Skin getByUrl(String skinUrl) {
        return skinRepository.findByUrl(skinUrl).orElseThrow(() -> new RuntimeException("Skin not found"));
    }

    public boolean isUnlocked(User user, Skin skin) {
        return userService.getStat(user, skin.getMetric()) >= skin.getRequirement();
    }
}
