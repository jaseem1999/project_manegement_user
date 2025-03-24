package com.pm.user.project_management.dto.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserCredential {
    private String email;
    private String officeNumber;
    private Long userAuthid;
}
