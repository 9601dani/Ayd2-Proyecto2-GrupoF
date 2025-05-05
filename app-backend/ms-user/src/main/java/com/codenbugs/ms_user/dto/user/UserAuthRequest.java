package com.codenbugs.ms_user.dto.user;

public record UserAuthRequest(String usernameOrEmail, String password) {
}
