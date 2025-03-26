package com.pm.user.project_management.controller.user;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.auth.*;
import com.pm.user.project_management.dto.response.user.*;
import com.pm.user.project_management.service.user.auth.UserAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/auth")
@Validated
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping(path = "/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addUser(
            @Valid @RequestBody AddUserRequest userRequest
            ){
        ApiResponse response = userAuthService.saveUser(userRequest);
        return new ResponseEntity<>(response,response.getStatus());
    }

    //TODO :: update Phone number
    //password update
    @PostMapping(path = "/password/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PasswordUpdateResponse>> updatePassword(
            @Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest
    ){
       ApiResponse<PasswordUpdateResponse> response = userAuthService.passwordUpdate(passwordUpdateRequest);
       return new ResponseEntity<>(response,response.getStatus());
    }

    @PostMapping(path = "/email/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<EmailUpdateResponse>> updateEmail(
            @Valid @RequestBody EmailUpdateRequest emailUpdateRequest
    ){
        ApiResponse<EmailUpdateResponse> response = userAuthService.emailUpdate(emailUpdateRequest);
        return new ResponseEntity<>(response,response.getStatus());
    }



    //TODO :: delete api;
    //TODO :: status change api;

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserDetailsResponse>> getOwnUserDetail(){
        ApiResponse<UserDetailsResponse> response = userAuthService.getOwnDetails();
        return new  ResponseEntity<>(response,response.getStatus());
    }

    @GetMapping(path = "/get/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<UserDetailsResponse>>> getAllUserDetails(){
        ApiResponse<List<UserDetailsResponse>> response = userAuthService.getAllUserDetails();
        return new  ResponseEntity<>(response,response.getStatus());
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserLoginResponse>> loginUser(
            @Valid @RequestBody UserLoginRequest loginRequest
            ){
        ApiResponse<UserLoginResponse> response = userAuthService.login(loginRequest);
        return new ResponseEntity<>(response,response.getStatus());
    }

    @PostMapping(path = "/refresh/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<UserRefreshTokenResponse>> verifyRefreshToken(
            @Valid @RequestBody UserRefreshTokenRequest requestRefreshToken
    ){
        ApiResponse<UserRefreshTokenResponse> response = userAuthService.verifyRefreshToken(requestRefreshToken);
        return new ResponseEntity<>(response,response.getStatus());
    }

}
