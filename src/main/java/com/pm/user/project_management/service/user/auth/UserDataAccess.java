package com.pm.user.project_management.service.user.auth;

import com.pm.user.project_management.dto.model.UserCredential;
import com.pm.user.project_management.dto.response.user.UserDetailsResponse;
import com.pm.user.project_management.entity.user.UserAuth;
import com.pm.user.project_management.entity.user.UserInfo;
import com.pm.user.project_management.exception.UserForbiddenException;
import com.pm.user.project_management.repository.bank.BankAccountRepository;
import com.pm.user.project_management.repository.user.UserAuthRepository;
import com.pm.user.project_management.repository.user.UserInfoRepository;
import com.pm.user.project_management.security.utls.JwtFilter;
import com.pm.user.project_management.security.utls.JwtUtils;
import io.jsonwebtoken.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDataAccess {

    private final BankAccountRepository bankAccountRepository;
    private final UserAuthRepository userAuthRepository;
    private final UserInfoRepository userInfoRepository;
    private final JwtFilter jwtFilter;
    private final JwtUtils jwtUtils;

    private UserCredential userCredential;

    public UserCredential getUserCredential() {
        return userCredential;
    }

    public void setUserCredential(UserCredential userCredential) {
        this.userCredential = userCredential;
    }

    public UserDataAccess(BankAccountRepository bankAccountRepository, UserAuthRepository userAuthRepository, UserInfoRepository userInfoRepository, JwtFilter jwtFilter, JwtUtils jwtUtils) {
        this.bankAccountRepository = bankAccountRepository;
        this.userAuthRepository = userAuthRepository;
        this.userInfoRepository = userInfoRepository;
        this.jwtFilter = jwtFilter;
        this.jwtUtils = jwtUtils;
    }

    public UserAuth getUserAuth(){
        String email = jwtFilter.getEmail();
        if (email == null){
            new RuntimeException("Jwt token validation failed, email not found");
        }
        System.out.println(getUserCredential().toString());
        UserAuth userAuth = userAuthRepository.findByEmail(email);
        if(userAuth.getAuthId() != getUserCredential().getUserAuthid()){
            throw new UserForbiddenException("User forbidden exception");
        }
        return userAuth;
    }

    public UserInfo getUserInfo(UserAuth userAuth){
        return userInfoRepository.findByUserAuth(userAuth).orElse(null);
    }

    public List<UserAuth> getAllUserAuth() {
        return userAuthRepository.findAll();
    }

    public UserDetailsResponse createUserDetailResponse(UserInfo userInfo) {
        return UserDetailsResponse.builder()
                .userId(userInfo.getId())
                .status(userInfo.getUserAuth().getStatus())
                .authId(userInfo.getUserAuth().getAuthId())
                .active(userInfo.getUserAuth().getActive())
                .email(userInfo.getUserAuth().getEmail())
                .officialNumber(userInfo.getUserAuth().getOfficialNumber())
                .fullName(userInfo.getFullName())
                .dob(userInfo.getDob())
                .gender(userInfo.getGender())
                .maritalStatus(userInfo.getMaritalStatus())
                .employeeId(userInfo.getEmployeeId())
                .designation(userInfo.getDesignation())
                .department(userInfo.getDepartment())
                .joiningDate(userInfo.getJoiningDate())
                .address(userInfo.getAddress())
                .city(userInfo.getCity())
                .state(userInfo.getState())
                .country(userInfo.getCountry())
                .personalEmail(userInfo.getPersonalEmail())
                .personalPhone(userInfo.getPersonalPhone())
                .createdAt(userInfo.getCreatedAt())
                .updatedAt(userInfo.getUpdatedAt())
                .createdBy(userInfo.getCreatedBy())
                .updatedBy(userInfo.getUpdatedBy())
                .adminUpdatedBy(userInfo.getAdminUpdatedBy())
                .build();
    }
}
