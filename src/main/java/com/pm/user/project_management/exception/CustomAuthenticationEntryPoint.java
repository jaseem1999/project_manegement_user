package com.pm.user.project_management.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        Map<String, Object> responseData = new HashMap<>();
        Map<String, String> message = new HashMap<>();
        int status = HttpServletResponse.SC_UNAUTHORIZED;

        if (authException instanceof BadCredentialsException) {
            message.put("message", "Invalid username or password.");
        } else if (authException instanceof InternalAuthenticationServiceException) {
            if(adminNotFound(authException)){
                message.put("message", "This email not registered.");
                status = HttpServletResponse.SC_NOT_FOUND;
            }else{
                message.put("message", "Internal server error occurred during authentication. "+authException.getMessage());
                status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
            }

        } else if (authException instanceof RuntimeException && isAuthenticationRelatedException(authException)) {
            message.put("message", "Authentication failed. "+authException.getMessage());
        } else {
            message.put("message", "An unexpected internal server error occurred. "+authException.getMessage());
            status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

        responseData.put("success", false);
        responseData.put("error", message);

        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseData));
    }

    private boolean adminNotFound(AuthenticationException exception) {
        System.err.println(exception.getMessage().toLowerCase().trim());
        return (exception.getMessage() != null && exception.getMessage().toLowerCase().trim().contains("userdetailsservice returned null"));
    }

    private boolean isAuthenticationRelatedException(Throwable exception) {
        return exception instanceof AuthenticationException
                || (exception.getMessage() != null && exception.getMessage().toLowerCase().contains("authentication"));
    }
}


