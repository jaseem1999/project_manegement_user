package com.pm.user.project_management.dto.response.user;

import com.pm.user.project_management.enums.user.UserActive;
import com.pm.user.project_management.enums.user.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginResponse {
    private Long userAuthId;
    private String officialNumber;
    private UserStatus status;
    private UserActive active;
    private String accessToken;
    private String refreshToken;
}
