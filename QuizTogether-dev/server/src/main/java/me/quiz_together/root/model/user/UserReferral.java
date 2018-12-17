package me.quiz_together.root.model.user;

import lombok.Data;

@Data
public class UserReferral {
    private Long userId;
    private Long referralUser;
}
