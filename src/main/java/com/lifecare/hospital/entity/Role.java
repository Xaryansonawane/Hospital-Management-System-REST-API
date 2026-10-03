package com.lifecare.hospital.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN,
    DOCTOR,
    PATIENT;

    @JsonCreator
    public static Role fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return PATIENT;
        }
        String clean = value.trim();
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(clean)) {
                return role;
            }
        }
        return Role.valueOf(clean.toUpperCase());
    }
}
