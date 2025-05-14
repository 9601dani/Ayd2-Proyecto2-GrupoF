package com.codenbugs.ms_report.clients;

import com.codenbugs.ms_report.dtos.user.UserResponse;
import com.codenbugs.ms_report.dtos.user.UserResponseWithName;
import com.codenbugs.ms_report.exceptions.user.UserNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-user", url = "http://localhost:8001/v1/users")
public interface UserRestClient {

    @GetMapping("/{id}")
    UserResponse findById(@PathVariable("id") Integer id) throws UserNotFoundException;

    @GetMapping("/role/{role}")
    List<UserResponseWithName> findUsersByRole(@PathVariable Integer role);

}
