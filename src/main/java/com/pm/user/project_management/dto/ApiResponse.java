package com.pm.user.project_management.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

public class ApiResponse<T> {
    private T data;
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private HttpStatus status;

    /**
     *  Avoids NullPointerException,
     *  metadata allows adding additional dynamic information to the API response without modifying the main structure.
     * {
     *     "data": [...],
     *     "success": true,
     *     "message": "Cases fetched successfully.",
     *     "status": "OK",
     *     "timestamp": "2024-02-11T12:34:56",
     *     "metadata": {
     *         "totalCases": 120,
     *         "currentPage": 1,
     *         "totalPages": 5
     *     }
     * }
     */
    private Map<String, Object> metadata = new TreeMap<>();

    // Default constructor for Jackson
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for simple success responses
    public ApiResponse(T data) {
        this.data = data;
        this.success = true;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for full responses
    public ApiResponse(T data, boolean success, String message, HttpStatus status) {
        this.data = data;
        this.success = success;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public T getData() { return data; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public HttpStatus getStatus() { return status; }
    public Map<String, Object> getMetadata() { return metadata; }

    // Setters
    public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }

}
