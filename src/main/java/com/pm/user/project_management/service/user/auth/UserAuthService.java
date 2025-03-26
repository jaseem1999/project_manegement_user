package com.pm.user.project_management.service.user.auth;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.auth.*;
import com.pm.user.project_management.dto.response.user.*;
import jakarta.validation.Valid;

import java.util.List;


public interface UserAuthService {
    ApiResponse saveUser(@Valid AddUserRequest userRequest);

    ApiResponse<UserLoginResponse> login(@Valid UserLoginRequest loginRequest);

    ApiResponse<UserRefreshTokenResponse> verifyRefreshToken(@Valid UserRefreshTokenRequest requestRefreshToken);

    ApiResponse<UserDetailsResponse> getOwnDetails();

    ApiResponse<List<UserDetailsResponse>> getAllUserDetails();

    ApiResponse<PasswordUpdateResponse> passwordUpdate(@Valid PasswordUpdateRequest passwordUpdateRequest);

    ApiResponse<EmailUpdateResponse> emailUpdate(@Valid EmailUpdateRequest emailUpdateRequest);
}
