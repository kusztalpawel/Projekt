package org.project.server.dto;

public class AuthResponseDTO {
    private String token;
    private String username;

    public AuthResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}