package me.quiz_together.root.model.broadcast;

import lombok.AllArgsConstructor;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum GiftType implements ValueEnum {
    PRIZE(100),
    GIFT(200);

    private int value;

    @Override
    public int getValue() {
        return value;
    }
}
