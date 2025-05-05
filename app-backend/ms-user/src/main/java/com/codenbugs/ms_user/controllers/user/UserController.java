package com.codenbugs.ms_user.controllers.user;

import com.codenbugs.ms_user.dto.user.UserAuthRequest;
import com.codenbugs.ms_user.dto.user.UserAuthenticatedResponse;
import com.codenbugs.ms_user.dto.user.UserResponse;
import com.codenbugs.ms_user.exceptions.user.UserException;
import com.codenbugs.ms_user.exceptions.user.UserNotFoundException;
import com.codenbugs.ms_user.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticatedResponse> authenticate(@RequestBody UserAuthRequest userAuthRequest) throws UserException {
        UserAuthenticatedResponse response = this.userService.authenticate(userAuthRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Integer id) throws UserNotFoundException {
        UserResponse response = this.userService.findById(id);
        return ResponseEntity.ok(response);
    }
}
