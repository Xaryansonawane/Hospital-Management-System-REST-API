package com.lifecare.hospital.service.impl;

import com.lifecare.hospital.dto.request.LoginRequest;
import com.lifecare.hospital.dto.request.RegisterRequest;
import com.lifecare.hospital.dto.response.AuthResponse;
import com.lifecare.hospital.entity.Patient;
import com.lifecare.hospital.entity.Role;
import com.lifecare.hospital.entity.User;
import com.lifecare.hospital.exception.BadRequestException;
import com.lifecare.hospital.exception.ResourceNotFoundException;
import com.lifecare.hospital.repository.PatientRepository;
import com.lifecare.hospital.repository.UserRepository;
import com.lifecare.hospital.security.JwtTokenProvider;
import com.lifecare.hospital.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("User already registered with this email!");
        }

        Role role = request.getRole() != null ? request.getRole() : Role.PATIENT;

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .aadhar(request.getAadhar())
                .dateOfBirth(request.getDob())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .department(request.getDoctrDptmnt())
                .doctorAvatar(request.getDoctorAvatar())
                .build();

        user = userRepository.save(user);

        // Auto-create patient record if registering as PATIENT
        if (role == Role.PATIENT) {
            long count = patientRepository.count() + 1;
            String generatedPatientId = String.format("PAT-2026-%05d", count);

            Patient patient = Patient.builder()
                    .patientId(generatedPatientId)
                    .userId(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .dateOfBirth(user.getDateOfBirth())
                    .gender(user.getGender())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();
            patientRepository.save(patient);
        }

        String token = jwtTokenProvider.generateToken(user);

        return AuthResponse.builder()
                .success(true)
                .message("User Registered Successfully")
                .token(token)
                .user(mapToSummary(user))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request.getConfirmPassword() != null && !request.getPassword().equals(request.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm Password do not match!");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid Password or Email!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException("Invalid Password or Email!");
        }

        if (request.getRole() != null && !request.getRole().equalsIgnoreCase(user.getRole().name())) {
            throw new BadRequestException("User with this role not found!");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(user);

        return AuthResponse.builder()
                .success(true)
                .message("User Login Successfully")
                .token(token)
                .user(mapToSummary(user))
                .build();
    }

    @Override
    public AuthResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return AuthResponse.builder()
                .success(true)
                .message("User Details Fetched")
                .user(mapToSummary(user))
                .build();
    }

    @Override
    @Transactional
    public User registerAdmin(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Admin with this email already exists!");
        }

        User admin = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .aadhar(request.getAadhar())
                .dateOfBirth(request.getDob())
                .gender(request.getGender())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        return userRepository.save(admin);
    }

    private AuthResponse.UserSummary mapToSummary(User user) {
        return AuthResponse.UserSummary.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .aadhar(user.getAadhar())
                .dob(user.getDateOfBirth())
                .gender(user.getGender())
                .role(user.getRole())
                .doctrDptmnt(user.getDepartment())
                .doctorAvatar(user.getDoctorAvatar())
                .build();
    }
}
