package me.quiz_together.root.model.broadcast;

import lombok.AllArgsConstructor;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum BroadcastType implements ValueEnum {
    PUBLIC(100),
    PRIVATE(200);

    private int value;

    @Override
    public int getValue() {
        return value;
    }
}
