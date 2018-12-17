package me.quiz_together.root.model.response.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import me.quiz_together.root.support.hashid.HashBroadcastId;
import me.quiz_together.root.support.hashid.HashUserId;

@Data
@Builder
public class UserProfileView {
    @ApiModelProperty(dataType = "java.lang.String")
    @HashUserId
    private Long userId;
    private String name;
    private String profilePath;
    private Long money;
    private BroadcastBeforeStarting broadcastBeforeStarting;
    private int heartCount;

    @Data
    @Builder
    public static class BroadcastBeforeStarting {
        @ApiModelProperty(dataType = "java.lang.String")
        @HashBroadcastId
        private Long broadcastId;
        private Long scheduledTime;
    }
}
