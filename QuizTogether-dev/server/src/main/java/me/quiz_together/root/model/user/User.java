package me.quiz_together.root.model.user;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String name;
    private String profilePath;
    private Long money;
    private Long createdTime;
    private Long updatedTime;
    private Long deletedTime;
    private UserStatus userStatus = UserStatus.NORMAL;
}
