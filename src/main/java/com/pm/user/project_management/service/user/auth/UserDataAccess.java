package com.pm.user.project_management.service.user.auth;

import com.pm.user.project_management.dto.model.UserCredential;
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
}
