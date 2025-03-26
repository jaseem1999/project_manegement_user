package com.pm.user.project_management.dto.model;

import com.pm.user.project_management.enums.user.Gender;
import com.pm.user.project_management.enums.user.MaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserData {
    private Long userId;
    private String fullName;
    private LocalDate dob;
    private Gender gender;
    private MaritalStatus maritalStatus;
    private String employeeId;
    private String designation;
    private String department;
    private LocalDate joiningDate;
    private String address;
    private String city;
    private String state;
    private String country;
    private String personalEmail;
    private String personalPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private Long adminUpdatedBy;
}
