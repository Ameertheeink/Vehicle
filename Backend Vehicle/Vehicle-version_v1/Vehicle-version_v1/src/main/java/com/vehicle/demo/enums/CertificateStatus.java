package com.vehicle.demo.enums;



public enum CertificateStatus {

    EXPIRED(1),
    EXPIRING_SOON(2),
    VALID(3);

    private final int priority;

    CertificateStatus(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}