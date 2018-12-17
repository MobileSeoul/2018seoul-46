package me.quiz_together.root.model.request.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class UserLoginRequest {
    @NotNull
    @ApiModelProperty("String")
    @HashUserId
    private Long userId;
}
