package com.nutritionangel.woi.enums;

public enum UserRole {
    ADMIN("관리자"),
    USER("일반사용자");

    private String label;

    private UserRole(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
