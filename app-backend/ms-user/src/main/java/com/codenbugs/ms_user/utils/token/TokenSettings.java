package com.codenbugs.ms_user.utils.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenSettings {
    private final String jwtSecret;
    private final Integer accessTokenExpiration;
    private final Integer refreshTokenExpiration;
    private final String zone;

}
