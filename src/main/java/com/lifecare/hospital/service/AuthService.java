package com.lifecare.hospital.service;

import com.lifecare.hospital.dto.request.LoginRequest;
import com.lifecare.hospital.dto.request.RegisterRequest;
import com.lifecare.hospital.dto.response.AuthResponse;
import com.lifecare.hospital.entity.User;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse getCurrentUser(String email);
    User registerAdmin(RegisterRequest request);
}
