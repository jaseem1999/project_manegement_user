package com.pm.user.project_management.exception;

public class UserForbiddenException extends RuntimeException {
    public UserForbiddenException(String message) {
        super(message);
    }
}
