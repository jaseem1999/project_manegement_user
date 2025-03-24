package com.pm.user.project_management.service.user.info;

import com.pm.user.project_management.dto.ApiResponse;
import com.pm.user.project_management.dto.request.auth.UpdateUserRequest;
import com.pm.user.project_management.dto.response.user.UserDetailsResponse;
import com.pm.user.project_management.entity.user.UserInfo;
import com.pm.user.project_management.repository.user.UserInfoRepository;
import com.pm.user.project_management.service.user.auth.UserDataAccess;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleStatus;
import java.time.LocalDateTime;

@Service
public class UserInfoServiceImpl implements UserInfoService{

    private final UserInfoRepository userInfoRepository;
    private final UserDataAccess userDataAccess;

    public UserInfoServiceImpl(UserInfoRepository userInfoRepository, UserDataAccess userDataAccess) {
        this.userInfoRepository = userInfoRepository;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public ApiResponse<UserDetailsResponse> update(UpdateUserRequest updateUserRequest) {
        UserInfo userInfo = userInfoRepository.findById(updateUserRequest.getUserId()).orElse(null);
        boolean isAdmin= userDataAccess.getUserAuth().getRole()
                .stream()
                .anyMatch(userRole -> userRole.getRole().equals("ADMIN"));

        //Admin can access all users
        //A user can only access their own data
        if (!isAdmin && userDataAccess.getUserAuth().getUserInfo().getId() != updateUserRequest.getUserId()) {
            return new ApiResponse<>(
                    null,
                    false,
                    "User Authorization failed",
                    HttpStatus.UNAUTHORIZED
            );
        }

        if(userInfo == null){
            return new ApiResponse<>(
                    null,
                    false,
                    "User info not founded by this id "+updateUserRequest.getUserId(),
                    HttpStatus.NOT_FOUND
            );
        }

        userInfo.setAddress(updateUserRequest.getAddress());
        userInfo.setCity(updateUserRequest.getCity());
        userInfo.setState(updateUserRequest.getState());
        userInfo.setCountry(updateUserRequest.getCountry());
        userInfo.setFullName(updateUserRequest.getFullName());
        userInfo.setPersonalEmail(updateUserRequest.getPersonalEmail());
        userInfo.setPersonalPhone(updateUserRequest.getPersonalPhone());
        userInfo.setPhoneNumber(updateUserRequest.getPhoneNumber());
        userInfo.setDob(updateUserRequest.getDob());
        userInfo.setGender(updateUserRequest.getGender());
        userInfo.setMaritalStatus(updateUserRequest.getMaritalStatus());
        userInfo.setEmployeeId(updateUserRequest.getEmployeeId());
        userInfo.setDesignation(updateUserRequest.getDesignation());
        userInfo.setDepartment(updateUserRequest.getDepartment());
        userInfo.setJoiningDate(updateUserRequest.getJoiningDate());

        if (isAdmin && userDataAccess.getUserAuth().getUserInfo().getId() == updateUserRequest.getUserId()) {
            userInfo.setAdminUpdatedBy(userDataAccess.getUserAuth().getAuthId());
            userInfo.setCreatedBy(userDataAccess.getUserAuth().getAuthId());
        }else if (isAdmin){
            userInfo.setAdminUpdatedBy(userDataAccess.getUserAuth().getAuthId());
        } else {
            userInfo.setCreatedBy(userDataAccess.getUserAuth().getAuthId());
        }
        userInfo.setUpdatedAt(LocalDateTime.now());
        try {
            userInfoRepository.save(userInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UserDetailsResponse detailsResponse=userDataAccess.createUserDetailResponse(userInfo);
        return new ApiResponse<>(detailsResponse,
                true,
                "User Details updated successful",
                HttpStatus.OK
                );
    }
}
