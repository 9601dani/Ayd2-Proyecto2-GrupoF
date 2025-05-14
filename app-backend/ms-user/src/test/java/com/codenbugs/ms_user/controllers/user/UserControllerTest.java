package com.codenbugs.ms_user.controllers.user;

import com.codenbugs.ms_user.builders.user.ConcreteUserBuilder;
import com.codenbugs.ms_user.dto.token.TokenResponse;
import com.codenbugs.ms_user.dto.user.*;
import com.codenbugs.ms_user.models.user.Role;
import com.codenbugs.ms_user.models.user.User;
import com.codenbugs.ms_user.services.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    private final String NAME_ROLE = "administrador";
    private final String FIRSTNAME = "firstname";
    private final String LASTNAME = "lastname";
    private final BigDecimal SALARY = new BigDecimal("100");
    private final Boolean ISENABLED = true;
    private User user;
    private TokenResponse token;
    private UserAuthenticatedResponse userAuthenticatedResponse;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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
    void findById() throws Exception {
        // Arrange
        UserResponse userResponse = new UserResponse(user);
        when(userService.findById(ID)).thenReturn(userResponse);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    void logout() throws Exception {
        mockMvc.perform(put("/v1/users/logout/{id}", ID))
                .andExpect(status().isOk());
        verify(userService).logout(ID);
    }

    @Test
    void findAll() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(ID);
        role.setName(NAME_ROLE);
        ListUserResponse userResponse = new ListUserResponse(
                ID, PHOTO, USERNAME, EMAIL,
                BigDecimal.valueOf(25.0), true,
                role, this.FIRSTNAME, this.LASTNAME
        );
        List<ListUserResponse> users = List.of(userResponse);
        when(userService.findAll()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(get("/v1/users/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }


    @Test
    void register() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest(USERNAME, EMAIL,FIRSTNAME, LASTNAME,SALARY,ID, PASSWORD);
        Role role = new Role();
        role.setId(ID);
        role.setName(NAME_ROLE);
        ListUserResponse response = new ListUserResponse(
                ID, PHOTO, USERNAME, EMAIL,
                BigDecimal.valueOf(25.0), true,
                role, FIRSTNAME, LASTNAME
        );
        when(userService.create(any(UserRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/v1/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void update() throws Exception {
        // Arrange
        UserRequest userRequest = new UserRequest(USERNAME, EMAIL,FIRSTNAME, LASTNAME,SALARY,ID, PASSWORD);
        Role role = new Role();
        role.setId(ID);
        role.setName(NAME_ROLE);
        ListUserResponse updatedUser = new ListUserResponse(
                ID, PHOTO, USERNAME, EMAIL,
                BigDecimal.valueOf(35.0), true,
                role, "Jane", "Doe"
        );
        when(userService.update(any(UserRequest.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(put("/v1/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }

    @Test
    void delete() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(ID);
        role.setName(NAME_ROLE);

        ListUserResponse deletedUser = new ListUserResponse(
                ID, PHOTO, USERNAME, EMAIL,
                BigDecimal.ZERO, false,
                role, FIRSTNAME, LASTNAME
        );

        when(userService.delete(USERNAME)).thenReturn(deletedUser);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/delete/{username}", USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(deletedUser)));

    }


    @Test
    void enable() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(ID);
        role.setName(NAME_ROLE);

        ListUserResponse enabledUser = new ListUserResponse(
                ID, PHOTO, USERNAME, EMAIL,
                SALARY, true,
                role, FIRSTNAME, LASTNAME
        );

        when(userService.enable(USERNAME)).thenReturn(enabledUser);

        // Act & Assert
        mockMvc.perform(put("/v1/users/enable")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(enabledUser)));

    }

//    @Test
//    void findUsersByRole() throws Exception {
//        // Arrange
//        UserResponseWithName userResponse = new UserResponseWithName(ID, USERNAME, FIRSTNAME, LASTNAME);
//        List<UserResponseWithName> users = List.of(userResponse);
//        when(userService.getUsersByRole(ID)).thenReturn(users);
//
//        // Act & Assert
//        mockMvc.perform(get("/v1/users/role/{role}", ID))
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(users)));
//    }

    @Test
    void findByUsername() throws Exception {
        // Arrange
        UserMyProfileResponse userProfile = new UserMyProfileResponse(
                ID, PHOTO, USERNAME, EMAIL, ISENABLED, FIRSTNAME, LASTNAME
        );
        when(userService.getUserByUsername(USERNAME)).thenReturn(userProfile);

        // Act & Assert
        mockMvc.perform(get("/v1/users/byUsername/{username}", USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userProfile)));
    }

    @Test
    void updateProfile() throws Exception {
        // Arrange
        UserMyProfile userRequest = new UserMyProfile(
                ID, PHOTO,USERNAME,EMAIL, ISENABLED, FIRSTNAME, LASTNAME, PASSWORD
        );
        UserMyProfileResponse updatedUser = new UserMyProfileResponse(
                ID, PHOTO, USERNAME, EMAIL, ISENABLED, FIRSTNAME, LASTNAME
        );
        when(userService.updateMyProfile(any(UserMyProfile.class))).thenReturn(updatedUser);

        // Act & Assert
        mockMvc.perform(put("/v1/users/myProfile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }

    @Test
    void updatePhotoPath() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "fake-image".getBytes());
        UserResponse response = new UserResponse(user);

        when(userService.updatePhotoPathUser(eq(ID), any(MultipartFile.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(multipart("/v1/users/update/photo_path/{id}", ID)
                        .file(file)
                        .with(request -> {
                            request.setMethod("PUT"); // override method to PUT
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}