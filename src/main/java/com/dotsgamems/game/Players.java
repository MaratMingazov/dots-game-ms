package com.dotsgamems.game;

import lombok.NonNull;

public enum Players {
    FIRST(1, "1"),
    SECOND(2,"2");

    private final Integer id;
    private final String dotLabel;

    Players(@NonNull Integer id, @NonNull String dotLabel) {
        this.id = id;
        this.dotLabel = dotLabel;
    }

    public String getDotLabel() {
        return this.dotLabel;
    }

    public static String getEmptyDotLabel() {
        return "0";
    }

    public static Players getById(@NonNull Integer id) {
        switch (id) {
            case 1: return FIRST;
            case 2: return SECOND;
            default: throw new IllegalArgumentException("The Player with given id does not exists");
        }
    }

    public static Players getOppositeById(@NonNull Integer id) {
        switch (id) {
            case 1: return SECOND;
            case 2: return FIRST;
            default: throw new IllegalArgumentException("The Player with given id does not exists");
        }
    }
}
