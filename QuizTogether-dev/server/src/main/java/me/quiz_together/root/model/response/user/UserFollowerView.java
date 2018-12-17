package me.quiz_together.root.model.response.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
@Builder
public class UserFollowerView {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long follower;
}
