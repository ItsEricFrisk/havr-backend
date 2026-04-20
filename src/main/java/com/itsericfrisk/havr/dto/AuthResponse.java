package com.itsericfrisk.havr.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
    public static AuthResponse response(String accessToken, Long expiresIn) {
        return new AuthResponse(accessToken, "Bearer", expiresIn);
    }
}
