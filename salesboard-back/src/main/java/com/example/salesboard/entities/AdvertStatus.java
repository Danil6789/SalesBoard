package com.example.salesboard.entities;

public enum AdvertStatus {
    ACTIVE("Активно"),
    COMPLETED("Завершено"),
    SOLD("Продано"),
    CLOSED("Закрыто");

    private final String displayName;
    AdvertStatus(String displayName) { this.displayName = displayName; }
    public String getDisplayName() { return displayName; }
}
