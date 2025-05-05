package com.codenbugs.ms_company.client;

import com.codenbugs.ms_company.dtos.user.UserResponse;
import com.codenbugs.ms_company.exceptions.UserNotFoundException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-user", url = "http://localhost:8001/v1/users")
public interface UserRestClient {

    @GetMapping("/{id}")
    UserResponse findById(@PathVariable("id") Integer id) throws UserNotFoundException;
}
