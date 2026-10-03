package com.lifecare.hospital.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AppointmentStatus {
    PENDING,
    CONFIRMED,
    CHECKED_IN,
    IN_CONSULTATION,
    COMPLETED,
    CANCELLED,
    NO_SHOW;

    @JsonCreator
    public static AppointmentStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return PENDING;
        }
        String clean = value.trim().toUpperCase();
        if ("ACCEPTED".equals(clean) || "APPROVED".equals(clean)) {
            return CONFIRMED;
        }
        if ("REJECTED".equals(clean)) {
            return CANCELLED;
        }
        for (AppointmentStatus s : AppointmentStatus.values()) {
            if (s.name().equalsIgnoreCase(clean)) {
                return s;
            }
        }
        return PENDING;
    }
}
