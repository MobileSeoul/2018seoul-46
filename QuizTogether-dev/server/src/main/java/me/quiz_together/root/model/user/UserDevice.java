package me.quiz_together.root.model.user;

import lombok.Data;

@Data
public class UserDevice {
    private Long id;
    private Long userId;
    private String pushToken;
    private Long created_time;
    private Long updated_time;
}
