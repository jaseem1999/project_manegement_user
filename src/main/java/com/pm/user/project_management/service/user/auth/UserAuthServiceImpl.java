package com.pm.user.project_management.service.user.auth;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.model.UserCredential;
import com.pm.user.project_management.dto.model.UserData;
import com.pm.user.project_management.dto.request.auth.*;
import com.pm.user.project_management.dto.response.user.*;
import com.pm.user.project_management.entity.user.RefreshToken;
import com.pm.user.project_management.entity.user.UserAuth;
import com.pm.user.project_management.entity.user.UserInfo;
import com.pm.user.project_management.entity.user.UserRole;
import com.pm.user.project_management.enums.user.UserActive;
import com.pm.user.project_management.enums.user.UserStatus;
import com.pm.user.project_management.repository.user.RefreshTokenRepository;
import com.pm.user.project_management.repository.user.UserAuthRepository;
import com.pm.user.project_management.repository.user.UserInfoRepository;
import com.pm.user.project_management.repository.user.UserRoleRepository;
import com.pm.user.project_management.security.utls.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserAuthServiceImpl implements UserAuthService {

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final UserAuthRepository userAuthRepository;
    private final UserInfoRepository userInfoRepository;
    private final UserRoleRepository userRoleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDataAccess userDataAccess;
    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;


    public UserAuthServiceImpl(UserAuthRepository userAuthRepository,
                               UserInfoRepository userInfoRepository,
                               UserRoleRepository userRoleRepository,
                               RefreshTokenRepository refreshTokenRepository, UserDataAccess userDataAccess,
                               JwtUtils jwtUtils,
                               AuthenticationManager authenticationManager) {
        this.userAuthRepository = userAuthRepository;
        this.userInfoRepository = userInfoRepository;
        this.userRoleRepository = userRoleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDataAccess = userDataAccess;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ApiResponse saveUser(AddUserRequest userRequest) {

        if(userAuthRepository.existsByEmail(userRequest.getEmail())){
            return new ApiResponse<>(null,false,
                    "This email already registered",HttpStatus.CONFLICT);
        }

        if (userAuthRepository.existsByOfficialNumber(userRequest.getOfficialNumber())){
            return new ApiResponse<>(null,false,
                    "This official number already registered",HttpStatus.CONFLICT);
        }

        if(userInfoRepository.existsByEmployeeId(userRequest.getInfo().getEmployeeId())){
            return new ApiResponse<>(null,false,
                    "This Employee id already registered",HttpStatus.CONFLICT);
        }

        if (userInfoRepository.existsByPersonalEmail(userRequest.getInfo().getPersonalEmail())){
            return new ApiResponse<>(null,false,
                    "This Personal email already registered",HttpStatus.CONFLICT);
        }

        if (userInfoRepository.existsByPhoneNumber(userRequest.getInfo().getPhoneNumber())){
            return new ApiResponse<>(null,false,
                    "This Personal phone number already registered",HttpStatus.CONFLICT);
        }


        UserAuth userAuth = new UserAuth();
        userAuth.setEmail(userRequest.getEmail());
        userAuth.setPassword(encoder.encode(userRequest.getPassword()));
        userAuth.setActive(UserActive.INACTIVE);
        userAuth.setStatus(UserStatus.ACTIVE);
        userAuth.setAdminUpdatedBy(0L);
        userAuth.setCreatedBy(0L);
        userAuth.setCreatedAt(LocalDateTime.now());
        userAuth.setOfficialNumber(userRequest.getOfficialNumber());

        userAuth = userAuthRepository.save(userAuth);

        UserInfo info = userInfoRequestDtoToUserInfoEntity(userAuth, userRequest.getInfo());

        userAuth.setUserInfo(info);

        Set<UserRole> roles = userRoleRequestDtoConvertToUserRoleEntity(userAuth, userRequest.getRoles());
        userAuth.getRole().clear();
        userAuth.getRole().addAll(roles);

        userAuth = userAuthRepository.save(userAuth);

        return new ApiResponse(null, true, "User added successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<UserLoginResponse> login(UserLoginRequest loginRequest) {
        Authentication authenticate =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            UserAuth userAuth=userAuthRepository.findByEmail(loginRequest.getEmail());
            userAuth.setActive(UserActive.ACTIVE);
            try {
                userAuth=userAuthRepository.save(userAuth);
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
            String jwtToken = jwtUtils.generateToken(userAuth.getEmail().toLowerCase());
            String refreshToken = createRefreshToken(userAuth).getToken();
            UserCredential userCredential=UserCredential.builder().officeNumber(userAuth.getOfficialNumber())
                    .userAuthid(userAuth.getAuthId())
                    .email(userAuth.getEmail())
                    .build();
            UserLoginResponse.UserLoginResponseBuilder userLoginResponseBuilder = UserLoginResponse.builder()
                    .userAuthId(userAuth.getAuthId())
                    .active(userAuth.getActive())
                    .status(userAuth.getStatus())
                    .officialNumber(userAuth.getOfficialNumber())
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken);

            if (userAuth.getUserInfo() != null ) {

                Map<String,Boolean> dataMap=verifyUserDataFullyCompleted(userAuth.getUserInfo());

                boolean completed=dataMap.values().stream().allMatch(value -> value);
                if (!completed){
                    userLoginResponseBuilder.isUserInfoFullyCompleted(true);
                    userLoginResponseBuilder.dataStatus(dataMap);
                }else {
                    userLoginResponseBuilder.isUserInfoFullyCompleted(false);
                }

                UserData userData = UserData.builder().userId(userAuth.getUserInfo().getId())
                        .fullName(userAuth.getUserInfo().getFullName())
                        .dob(userAuth.getUserInfo().getDob())
                        .gender(userAuth.getUserInfo().getGender())
                        .maritalStatus(userAuth.getUserInfo().getMaritalStatus())
                        .employeeId(userAuth.getUserInfo().getEmployeeId())
                        .designation(userAuth.getUserInfo().getDesignation())
                        .department(userAuth.getUserInfo().getDepartment())
                        .joiningDate(userAuth.getUserInfo().getJoiningDate())
                        .address(userAuth.getUserInfo().getAddress())
                        .city(userAuth.getUserInfo().getCity())
                        .state(userAuth.getUserInfo().getState())
                        .country(userAuth.getUserInfo().getCountry())
                        .personalEmail(userAuth.getUserInfo().getPersonalEmail())
                        .personalPhone(userAuth.getUserInfo().getPersonalPhone())
                        .build();
                userDataAccess.setUserData(userData);
                userLoginResponseBuilder.isUserDataAddRequired(false);
            }else{
                userLoginResponseBuilder.isUserDataAddRequired(true);
            }
            userDataAccess.setUserCredential(userCredential);

            UserLoginResponse userLoginResponse= userLoginResponseBuilder.build();

            return new ApiResponse<>(userLoginResponse
                    ,true
                    ,"Login successful"
                    ,HttpStatus.OK);
        }
        return new ApiResponse<>(null
                ,false
                ,"Invalid email or password"
                ,HttpStatus.OK);
    }


    public Map<String, Boolean> verifyUserDataFullyCompleted(UserInfo userInfo) {
        Map<String, Boolean> responseMap = new HashMap<>();

        responseMap.put("fullName", userInfo.getFullName() != null && !userInfo.getFullName().isEmpty());
        responseMap.put("phoneNumber", userInfo.getPhoneNumber() != null && !userInfo.getPhoneNumber().isEmpty());
        responseMap.put("dob", userInfo.getDob() != null);
        responseMap.put("gender", userInfo.getGender() != null);
        responseMap.put("maritalStatus", userInfo.getMaritalStatus() != null);
        responseMap.put("employeeId", userInfo.getEmployeeId() != null && !userInfo.getEmployeeId().isEmpty());
        responseMap.put("designation", userInfo.getDesignation() != null && !userInfo.getDesignation().isEmpty());
        responseMap.put("department", userInfo.getDepartment() != null && !userInfo.getDepartment().isEmpty());
        responseMap.put("joiningDate", userInfo.getJoiningDate() != null);
        responseMap.put("address", userInfo.getAddress() != null && !userInfo.getAddress().isEmpty());
        responseMap.put("city", userInfo.getCity() != null && !userInfo.getCity().isEmpty());
        responseMap.put("state", userInfo.getState() != null && !userInfo.getState().isEmpty());
        responseMap.put("country", userInfo.getCountry() != null && !userInfo.getCountry().isEmpty());
        responseMap.put("personalEmail", userInfo.getPersonalEmail() != null && !userInfo.getPersonalEmail().isEmpty());
        responseMap.put("personalPhone", userInfo.getPersonalPhone() != null && !userInfo.getPersonalPhone().isEmpty());

        return responseMap;
    }


    @Override
    public ApiResponse<UserRefreshTokenResponse> verifyRefreshToken(UserRefreshTokenRequest requestRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(requestRefreshToken.getToken()).orElse(null);
        if (refreshToken == null){
            return new ApiResponse<>(null,
                    false,
                    "token validation failed",
                    HttpStatus.FORBIDDEN);
        }

        if (refreshToken.getExpiredAt().compareTo(Instant.now()) < 0){
            return new ApiResponse<>(null,
                    false,
                    "Token expired please login",
                    HttpStatus.REQUEST_TIMEOUT
                    );
        }

        String jwtToken=jwtUtils.generateToken(refreshToken.getUserAuth().getEmail().toLowerCase());
        String newRefreshToken=createRefreshToken(refreshToken.getUserAuth()).getToken();

        return new ApiResponse<>(
                UserRefreshTokenResponse.builder()
                        .accessToken(jwtToken)
                        .refreshToken(newRefreshToken)
                        .build(),
                true,
                "Verification successful",
                HttpStatus.OK
        );
    }

    @Override
    public ApiResponse<UserDetailsResponse> getOwnDetails() {
        UserAuth userAuth = userDataAccess.getUserAuth();

        UserDetailsResponse.UserDetailsResponseBuilder detailsResponseBuilder = UserDetailsResponse.builder()
                .active(userAuth.getActive())
                .authId(userAuth.getAuthId())
                .email(userAuth.getEmail())
                .officialNumber(userAuth.getOfficialNumber())
                .status(userAuth.getStatus())
                .createdAt(userAuth.getCreatedAt())
                .createdBy(userAuth.getCreatedBy())
                .updatedAt(userAuth.getUpdatedAt())
                .adminUpdatedBy(userAuth.getAdminUpdatedBy());

        if (userAuth.getUserInfo() != null) {
            UserInfo userInfo = userAuth.getUserInfo();
            detailsResponseBuilder
                    .userId(userInfo.getId())
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
                    .personalPhone(userInfo.getPersonalPhone());
        }
        UserDetailsResponse detailsResponse = detailsResponseBuilder.build();

        return new ApiResponse<>(detailsResponse,
                true,
                "Information fetched successful",
                HttpStatus.OK);
    }

    @Override
    public ApiResponse<List<UserDetailsResponse>> getAllUserDetails() {
        List<UserAuth> userAuthList = userDataAccess.getAllUserAuth(); // Assume this fetches all users

        List<UserDetailsResponse> userDetailsResponses = userAuthList.stream().map(userAuth -> {
            UserDetailsResponse.UserDetailsResponseBuilder detailsResponseBuilder = UserDetailsResponse.builder()
                    .active(userAuth.getActive())
                    .authId(userAuth.getAuthId())
                    .email(userAuth.getEmail())
                    .officialNumber(userAuth.getOfficialNumber())
                    .status(userAuth.getStatus())
                    .createdAt(userAuth.getCreatedAt())
                    .createdBy(userAuth.getCreatedBy())
                    .updatedAt(userAuth.getUpdatedAt())
                    .adminUpdatedBy(userAuth.getAdminUpdatedBy());

            if (userAuth.getUserInfo() != null) {
                UserInfo userInfo = userAuth.getUserInfo();
                detailsResponseBuilder
                        .userId(userInfo.getId())
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
                        .personalPhone(userInfo.getPersonalPhone());
            }

            return detailsResponseBuilder.build();
        }).collect(Collectors.toList());

        return new ApiResponse<>(userDetailsResponses, true, "Users fetched successfully", HttpStatus.OK);
    }

    @Override
    public ApiResponse<PasswordUpdateResponse> passwordUpdate(PasswordUpdateRequest passwordUpdateRequest) {
        UserAuth userAuth=userDataAccess.getUserAuth();
        if (!encoder.matches(passwordUpdateRequest.getOldPassword(),userAuth.getPassword())){
            return new ApiResponse<>(
                    PasswordUpdateResponse.builder()
                            .message("Password mismatch")
                            .isLoginRequired(false)
                            .build(),
                    false,
                    "Invalid password",
                    HttpStatus.FORBIDDEN
            );
        }

        if (passwordUpdateRequest.getOldPassword().equals(userAuth.getPassword())){
            return new ApiResponse<>(
                    PasswordUpdateResponse.builder()
                            .message("Old password and new password is same")
                            .isLoginRequired(false)
                            .build(),
                    false,
                    "Old password and new password is same",
                    HttpStatus.NOT_ACCEPTABLE
            );
        }

        userAuth.setPassword(encoder.encode(passwordUpdateRequest.getNewPassword()));
        try {
            userAuthRepository.save(userAuth);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ApiResponse<>( PasswordUpdateResponse.builder()
                .message("Login required")
                .isLoginRequired(true)
                .build(),
                true,
                "Password updated successful",
                HttpStatus.OK);
    }

    @Override
    public ApiResponse<EmailUpdateResponse> emailUpdate(EmailUpdateRequest emailUpdateRequest) {
        return null;
    }


    private RefreshToken createRefreshToken(UserAuth userAuth) {
        RefreshToken refreshToken= refreshTokenRepository
                .findByUserAuth(userAuth).orElse(new RefreshToken());
        refreshToken.setUserAuth(userAuth);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiredAt(Instant.now().plus(3, ChronoUnit.MINUTES));

        return refreshTokenRepository.save(refreshToken);
    }



    private Set<UserRole> userRoleRequestDtoConvertToUserRoleEntity(UserAuth userAuth, Set<AddRoleRequest> roles) {
        Set<UserRole> userRoles = new HashSet<>();
        LocalDateTime now = LocalDateTime.now();

        for (AddRoleRequest roleRequest : roles) {
            UserRole userRole = new UserRole(
                    roleRequest.getRole(),
                    now,
                    now,
                    0L,
                    0L,
                    userAuth
            );
            userRoles.add(userRole);
        }

        return new HashSet<>(userRoleRepository.saveAll(userRoles));
    }


    private UserInfo userInfoRequestDtoToUserInfoEntity(UserAuth userAuth, AddInfoRequest info) {
        return new UserInfo(
                info.getAddress(),
                info.getCity(),
                info.getCountry(),
                info.getDob(),
                info.getDepartment(),
                info.getDesignation(),
                info.getEmployeeId(),
                info.getFullName(),
                info.getGender(),
                info.getJoiningDate(),
                info.getMaritalStatus(),
                info.getPersonalEmail(),
                info.getPersonalPhone(),
                info.getPhoneNumber(),
                info.getState(),
                userAuth,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null, null, null
        );
    }
}

