package com.pm.user.project_management.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRefreshTokenResponse {
    private String accessToken;
    private String refreshToken;
}
