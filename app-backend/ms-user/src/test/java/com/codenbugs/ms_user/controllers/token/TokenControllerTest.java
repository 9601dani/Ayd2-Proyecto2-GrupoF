package com.codenbugs.ms_user.controllers.token;

import com.codenbugs.ms_user.dto.token.RefreshTokenRequest;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.dto.user.UserAuthRequest;
import com.codenbugs.ms_user.exceptions.user.UserException;
import com.codenbugs.ms_user.services.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TokenController.class)
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenService tokenService;

    private final Integer ID = 1;
    private final String TOKEN = "token";
    private final String ACCESS_TOKEN = "ACCESS_TOKEN";

    private RefreshTokenRequest refreshTokenRequest;
    private TokenResponse response;

    @BeforeEach
    void setUp() {
        refreshTokenRequest = new RefreshTokenRequest(ID, TOKEN);
        response = new TokenResponse(ACCESS_TOKEN, TOKEN);
    }


    @Test
    void createToken() throws Exception {
        String request = String.format("""
                {
                    "id": %d,
                    "token": "%s"
                }
                """, ID, TOKEN);

        String jsonResponse = String.format("""
                {
                    "accessToken": "%s",
                    "refreshToken": "%s"
                }
                """, ACCESS_TOKEN, TOKEN);

        when(this.tokenService.refreshToken(refreshTokenRequest)).thenReturn(response);

        mockMvc.perform(post("/v1/tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(jsonResponse));
    }
}