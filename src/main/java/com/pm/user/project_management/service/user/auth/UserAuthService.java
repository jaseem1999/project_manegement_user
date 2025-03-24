package com.pm.user.project_management.service.user.auth;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.auth.AddUserRequest;
import com.pm.user.project_management.dto.request.auth.UserLoginRequest;
import com.pm.user.project_management.dto.request.auth.UserRefreshTokenRequest;
import com.pm.user.project_management.dto.response.user.UserDetailsResponse;
import com.pm.user.project_management.dto.response.user.UserLoginResponse;
import com.pm.user.project_management.dto.response.user.UserRefreshTokenResponse;
import jakarta.validation.Valid;

import java.util.List;


public interface UserAuthService {
    ApiResponse saveUser(@Valid AddUserRequest userRequest);

    ApiResponse<UserLoginResponse> login(@Valid UserLoginRequest loginRequest);

    ApiResponse<UserRefreshTokenResponse> verifyRefreshToken(@Valid UserRefreshTokenRequest requestRefreshToken);

    ApiResponse<UserDetailsResponse> getOwnDetails();

    ApiResponse<List<UserDetailsResponse>> getAllUserDetails();
}
