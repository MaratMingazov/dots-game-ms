package com.dotsgamems;

public enum Players {
    FIRST("1"),
    SECOND("*");

    private final String dotLabel;

    Players(String dotLabel) {
        this.dotLabel = dotLabel;
    }

    public String getDotLabel() {
        return this.dotLabel;
    }
}
