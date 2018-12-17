package me.quiz_together.root.model.user;

import lombok.AllArgsConstructor;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum PlayUserStatus implements ValueEnum {
    PLAYER(100),
    LOSER(200),
    VIEWER(300);

    private int value;

    @Override
    public int getValue() {
        return value;
    }
}
