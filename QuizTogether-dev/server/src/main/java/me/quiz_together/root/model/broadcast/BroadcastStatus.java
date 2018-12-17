package me.quiz_together.root.model.broadcast;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.quiz_together.root.support.enumeration.ValueEnum;

@AllArgsConstructor
public enum BroadcastStatus implements ValueEnum {
    CREATED(100, true),
    WATING(200, true),
    OPEN_QUESTION(300, true),
    OPEN_ANSWER(400, true),
    OPEN_WINNER(500, true),
    COMPLETED(600, false);

    private int value;
    @Getter
    private boolean accessibleBroadcast;

    @Override
    public int getValue() {
        return value;
    }


}
