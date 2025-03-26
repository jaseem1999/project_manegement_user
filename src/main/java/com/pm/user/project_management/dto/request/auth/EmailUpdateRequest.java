package com.pm.user.project_management.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailUpdateRequest {
    @NotNull
    @NotBlank
    private String password;
    @Email(message = "Invalid email format")
    private String email;
}
