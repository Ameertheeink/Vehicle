package com.vehicle.demo.enums;

public enum ReminderStatus {

    OVERDUE(1, "Overdue"),
    DUE_SOON(2, "Due Soon"),
    SAFE(3, "Safe");

    private final int priority;
    private final String label;

    ReminderStatus(int priority, String label) {
        this.priority = priority;
        this.label = label;
    }

    public int getPriority() {
        return priority;
    }

    public String getLabel() {
        return label;
    }
}