package org.project.server.dto;

import org.project.server.model.PowerUpType;

public record PowerUpDTO(Long id, String name, String description, Integer price, PowerUpType type, Integer value, Integer ownedAmount) {
}
