package com.pm.user.project_management.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "role_table")
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    private String role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long adminCreatedBy;

    private Long adminUpdatedBy;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private UserAuth auth;

    public UserRole() {
    }

    public UserRole(String role, LocalDateTime createdAt, LocalDateTime updatedAt, Long adminCreatedBy, Long adminUpdatedBy, UserAuth auth) {
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.adminCreatedBy = adminCreatedBy;
        this.adminUpdatedBy = adminUpdatedBy;
        this.auth = auth;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getAdminCreatedBy() {
        return adminCreatedBy;
    }

    public void setAdminCreatedBy(Long adminCreatedBy) {
        this.adminCreatedBy = adminCreatedBy;
    }

    public Long getAdminUpdatedBy() {
        return adminUpdatedBy;
    }

    public void setAdminUpdatedBy(Long adminUpdatedBy) {
        this.adminUpdatedBy = adminUpdatedBy;
    }

    public UserAuth getAuth() {
        return auth;
    }

    public void setAuth(UserAuth auth) {
        this.auth = auth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(roleId, userRole.roleId) && Objects.equals(role, userRole.role) && Objects.equals(auth, userRole.auth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, role, auth);
    }
}
