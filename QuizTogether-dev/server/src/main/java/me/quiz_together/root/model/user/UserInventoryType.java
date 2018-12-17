package me.quiz_together.root.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum UserInventoryType {
    HEART(1);

    @Getter
    private int reward;

}
