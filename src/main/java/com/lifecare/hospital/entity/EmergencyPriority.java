package com.lifecare.hospital.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EmergencyPriority {
    CRITICAL,
    HIGH,
    MEDIUM,
    LOW;

    @JsonCreator
    public static EmergencyPriority fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return MEDIUM;
        }
        String clean = value.trim().toUpperCase();
        if ("RESUSCITATION".equals(clean) || "EMERGENT".equals(clean)) {
            return CRITICAL;
        }
        if ("URGENT".equals(clean)) {
            return HIGH;
        }
        if ("LESS_URGENT".equals(clean)) {
            return MEDIUM;
        }
        if ("NON_URGENT".equals(clean)) {
            return LOW;
        }
        for (EmergencyPriority ep : EmergencyPriority.values()) {
            if (ep.name().equalsIgnoreCase(clean)) {
                return ep;
            }
        }
        return MEDIUM;
    }
}
