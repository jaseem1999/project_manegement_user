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



        return null;
    }
}
