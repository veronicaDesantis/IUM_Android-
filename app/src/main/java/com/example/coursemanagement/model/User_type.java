package com.example.coursemanagement.model;

public enum User_type {
    STUDENT(1),
    TEACHER(2),
    ADMIN(3);

    private final int value;
    private User_type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
