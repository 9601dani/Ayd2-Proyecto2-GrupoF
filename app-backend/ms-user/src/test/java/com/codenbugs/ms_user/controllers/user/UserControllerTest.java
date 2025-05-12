package com.codenbugs.ms_user.controllers.user;

import com.codenbugs.ms_user.builders.user.ConcreteUserBuilder;
import com.codenbugs.ms_user.builders.user.UserBuilder;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.dto.user.UserAuthRequest;
import com.codenbugs.ms_user.dto.user.UserAuthenticatedResponse;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    private final Integer ID = 1;
    private final String USERNAME = "username";
    private final String PHOTO = "photo";
    private final String TOKEN = "token";
    private final String REFRESH_TOKEN = "refresh_token";
    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    private User user;
    private TokenResponse token;
    private UserAuthenticatedResponse userAuthenticatedResponse;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        user = new ConcreteUserBuilder()
                .withId(ID)
                .withUsername(USERNAME)
                .withPhoto(PHOTO)
                .withToken(TOKEN)
                .withEmail(EMAIL)
                .withPassword(PASSWORD)
                .build();

        token = new TokenResponse(TOKEN, REFRESH_TOKEN);
        userAuthenticatedResponse = new UserAuthenticatedResponse(user, token);
    }


    @Test
    void authenticate() throws Exception {
        UserAuthRequest userAuthRequest = new UserAuthRequest(USERNAME, PASSWORD);
        String requestBody = "{\"usernameOrEmail\":\"" + USERNAME + "\",\"password\":\"" + PASSWORD + "\"}";
        String response = String.format("""
        {
          "id": %d,
          "username": "%s",
          "photo": "%s",
          "token": {
            "accessToken": "%s",
            "refreshToken": "%s"
          }
        }
        """, ID, USERNAME, PHOTO, TOKEN, REFRESH_TOKEN);

        when(this.userService.authenticate(userAuthRequest)).thenReturn(userAuthenticatedResponse);
        mockMvc.perform(post("/v1/users/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(response));
    }

    @Test
    void findById() {
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(get("/v1/users/" + ID))
                .andExpect(status().isOk());
    }

    @Test
    void findAll() {
    }

    @Test
    void register() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void enable() {
    }

    @Test
    void findUsersByRole() {
    }

    @Test
    void findByUsername() {
    }

    @Test
    void updateProfile() {
    }

    @Test
    void updatePhotoPath() {
    }
}