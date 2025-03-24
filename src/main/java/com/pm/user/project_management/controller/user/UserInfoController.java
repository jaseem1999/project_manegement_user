package com.pm.user.project_management.controller.user;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.auth.UpdateUserRequest;
import com.pm.user.project_management.dto.response.user.UserDetailsResponse;
import com.pm.user.project_management.service.user.info.UserInfoService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/info")
@Validated
public class UserInfoController {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserDetailsResponse>> updateUser(
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ){
        ApiResponse<UserDetailsResponse> response = userInfoService.update(updateUserRequest);
        return new ResponseEntity<>(response,response.getStatus());
    }
}
