package me.quiz_together.root.model.firebase;

import lombok.Builder;
import lombok.Getter;
import me.quiz_together.root.model.broadcast.BroadcastStatus;

@Builder
@Getter
public class BroadcastPlayInfoMessage implements FcmMessage {
    private BroadcastStatus broadcastStatus;
    private Long viewerCount;
    private PushType pushType;
}
