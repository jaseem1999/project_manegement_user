package com.pm.user.project_management.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.pm.user.project_management.enums.user.Gender;
import com.pm.user.project_management.enums.user.MaritalStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personal Details
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "dob")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    // Employment Details
    @Column(name = "employee_id", unique = true)
    private String employeeId;

    @Column(name = "designation")
    private String designation;

    @Column(name = "department")
    private String department;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    // Address & Location
    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long createdBy;

    private Long updatedBy;

    private Long adminUpdatedBy;

    @Column(name = "personal_email", unique = true)
    private String personalEmail;

    @Column(name = "personal_phone")
    private String personalPhone;

    @JsonIgnore
    @OneToOne(mappedBy = "userInfo",cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private UserAuth userAuth;

    public UserInfo() {
    }

    public UserInfo(String address, String city, String country, LocalDate dob, String department, String designation,
                    String employeeId, String fullName, Gender gender, LocalDate joiningDate, MaritalStatus maritalStatus,
                    String personalEmail, String personalPhone, String phoneNumber, String state, UserAuth userAuth,
                    LocalDateTime createdAt, LocalDateTime updatedAt, Long createdBy, Long updatedBy, Long adminUpdatedBy) {
        this.address = address;
        this.city = city;
        this.country = country;
        this.dob = dob;
        this.department = department;
        this.designation = designation;
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.gender = gender;
        this.joiningDate = joiningDate;
        this.maritalStatus = maritalStatus;
        this.personalEmail = personalEmail;
        this.personalPhone = personalPhone;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.userAuth = userAuth;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.adminUpdatedBy = adminUpdatedBy;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Long getAdminUpdatedBy() {
        return adminUpdatedBy;
    }

    public void setAdminUpdatedBy(Long adminUpdatedBy) {
        this.adminUpdatedBy = adminUpdatedBy;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getPersonalPhone() {
        return personalPhone;
    }

    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }

    public UserAuth getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(UserAuth userAuth) {
        this.userAuth = userAuth;
    }
}
