package me.quiz_together.root.model.firebase;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatMessage implements FcmMessage {
    private String message;
    private String userName;
    private PushType pushType;

}
