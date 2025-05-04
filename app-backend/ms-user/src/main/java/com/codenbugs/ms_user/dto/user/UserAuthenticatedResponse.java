package com.codenbugs.ms_user.dto.user;

public record UserAuthenticatedResponse(String username, TokenResponse token) {
}
