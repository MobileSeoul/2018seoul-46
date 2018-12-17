package me.quiz_together.root.model.request.broadcast;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class SendHeartRequest {
    @NotNull
    private Integer step;
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
}
