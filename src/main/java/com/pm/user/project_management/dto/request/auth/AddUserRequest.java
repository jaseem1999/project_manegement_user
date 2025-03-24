package com.pm.user.project_management.dto.request.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.TreeSet;

public class AddUserRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$",
            message = "Password must contain at least one letter, one number, and one special character"
    )
    private String password;
    @NotBlank(message = "Official number is required")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Official number must be 10-15 digits, with an optional '+' prefix"
    )
    private String officialNumber;

    @Valid
    private Set<AddRoleRequest> roles = new TreeSet<>();

    private AddInfoRequest info;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOfficialNumber() {
        return officialNumber;
    }

    public void setOfficialNumber(String officialNumber) {
        this.officialNumber = officialNumber;
    }

    public Set<AddRoleRequest> getRoles() {
        return roles;
    }

    public void setRoles(Set<AddRoleRequest> roles) {
        this.roles = roles;
    }

    public AddInfoRequest getInfo() {
        return info;
    }

    public void setInfo(AddInfoRequest info) {
        this.info = info;
    }
}
