package me.quiz_together.root.model.firebase;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EndBroadcastMessage implements FcmMessage {
    private PushType pushType;
}
