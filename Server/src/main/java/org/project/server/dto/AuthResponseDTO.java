package org.project.server.dto;

public record AuthResponseDTO (String token, String username, Integer points){
}