package me.quiz_together.root.model.user;

import lombok.Data;

@Data
public class UserFollower {
    private Long userId;
    private Long follower;
}
