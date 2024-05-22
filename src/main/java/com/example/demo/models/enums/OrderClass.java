package com.example.demo.models.enums;

public enum OrderClass {
    ONLINE("Online"),
    ON_STORE("On Store");

    private final String displayName;

    OrderClass(String displayName) {
        this.displayName = displayName;
    }


}
