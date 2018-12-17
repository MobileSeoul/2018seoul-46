package me.quiz_together.root.model.request.firebase;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
public class ChatMessageRequest {
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    @NotNull
    @ApiModelProperty(dataType = "java.lang.String")
    @HashBroadcastId
    private Long broadcastId;
    @NotNull
    private String message;
}
