package me.quiz_together.root.model.request.user;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserSignupRequest {
    @NotNull
    private String name;
    @NotNull
    private String pushToken;
}
