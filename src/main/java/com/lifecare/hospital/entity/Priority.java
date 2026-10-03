package com.lifecare.hospital.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Priority {
    LOW,
    MEDIUM,
    HIGH;

    @JsonCreator
    public static Priority fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return MEDIUM;
        }
        String clean = value.trim().toUpperCase();
        if ("REGULAR".equals(clean) || "NORMAL".equals(clean)) {
            return LOW;
        }
        if ("URGENT".equals(clean) || "CRITICAL".equals(clean) || "EMERGENCY".equals(clean)) {
            return HIGH;
        }
        for (Priority p : Priority.values()) {
            if (p.name().equalsIgnoreCase(clean)) {
                return p;
            }
        }
        return MEDIUM;
    }
}
