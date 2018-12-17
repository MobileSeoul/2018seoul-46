package me.quiz_together.root.model.request.user;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class UserReferralRequest {
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long referralUser;
}
