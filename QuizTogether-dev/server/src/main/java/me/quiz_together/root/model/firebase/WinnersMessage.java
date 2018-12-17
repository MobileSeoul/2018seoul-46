package me.quiz_together.root.model.firebase;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import me.quiz_together.root.model.broadcast.GiftType;

@Builder
@Getter
public class WinnersMessage implements FcmMessage {
    private String winnerMessage;
    private List<String> userName;
    private Long prize;
    private String giftDescription;
    private GiftType giftType;
    private PushType pushType;
}
