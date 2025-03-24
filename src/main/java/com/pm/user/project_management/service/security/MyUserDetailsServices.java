package com.pm.user.project_management.service.security;

import com.pm.user.project_management.entity.user.UserAuth;
import com.pm.user.project_management.repository.user.UserAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServices implements UserDetailsService {

    @Autowired
    private UserAuthRepository userAuthRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAuth userAuth = userAuthRepository.findByEmail(email);
        if(userAuth == null){
            System.err.println("User not found...");
            throw new UsernameNotFoundException("User not found....");
        }
        return new UserPrincipal(userAuth);
    }
}
