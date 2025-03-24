package com.pm.user.project_management.service.user.info;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.auth.UpdateUserRequest;
import com.pm.user.project_management.dto.response.user.UserDetailsResponse;
import jakarta.validation.Valid;

public interface UserInfoService {
    ApiResponse<UserDetailsResponse> update(@Valid UpdateUserRequest updateUserRequest);
}
