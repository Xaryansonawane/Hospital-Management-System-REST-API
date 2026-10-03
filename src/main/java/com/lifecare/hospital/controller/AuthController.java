package com.lifecare.hospital.controller;

import com.lifecare.hospital.dto.request.LoginRequest;
import com.lifecare.hospital.dto.request.RegisterRequest;
import com.lifecare.hospital.dto.response.ApiResponse;
import com.lifecare.hospital.dto.response.AuthResponse;
import com.lifecare.hospital.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration, login, and profile fetching")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user / patient")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.register(request);
        setAuthCookie(response, authResponse.getToken(), request.getRole() != null ? request.getRole().name() : "PATIENT");
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and issue JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        AuthResponse authResponse = authService.login(request);
        setAuthCookie(response, authResponse.getToken(), authResponse.getUser().getRole().name());
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "Get currently authenticated user details")
    public ResponseEntity<AuthResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.getCurrentUser(userDetails.getUsername()));
    }

    private void setAuthCookie(HttpServletResponse response, String token, String role) {
        String cookieName = "ADMIN".equalsIgnoreCase(role) ? "adminToken" : "patientToken";
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 24 hours
        response.addCookie(cookie);

        // Also add generic token cookie
        Cookie generic = new Cookie("token", token);
        generic.setHttpOnly(true);
        generic.setPath("/");
        generic.setMaxAge(86400);
        response.addCookie(generic);
    }
}
