package com.lifecare.hospital.dto.response;

import com.lifecare.hospital.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    @Builder.Default
    private Boolean success = true;
    private String message;
    private String token;
    private UserSummary user;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummary {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String aadhar;
        private LocalDate dob;
        private String gender;
        private Role role;
        private String doctrDptmnt;
        private String doctorAvatar;
    }
}
