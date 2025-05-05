package com.codenbugs.ms_user.controllers.token;

import com.codenbugs.ms_user.dto.token.RefreshTokenRequest;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.exceptions.user.UserException;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.services.token.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/tokens")
@AllArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    @PostMapping()
    public ResponseEntity<TokenResponse> createToken(@RequestBody RefreshTokenRequest tokenRequest) throws UserException {
        TokenResponse tokenResponse = this.tokenService.refreshToken(tokenRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenResponse);
    }
}
