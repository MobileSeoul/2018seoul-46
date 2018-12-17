package me.quiz_together.root.model.user;

import me.quiz_together.root.support.enumeration.ValueEnum;

public enum UserStatus implements ValueEnum {
    NORMAL(0),
    DELETED(1);

    int value = 0;

    private UserStatus(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }

}
