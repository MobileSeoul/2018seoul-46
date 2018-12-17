package me.quiz_together.root.model.broadcast;

import lombok.Data;

@Data
public class Broadcast {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private BroadcastStatus broadcastStatus;
    private Long prize;
    private String giftDescription;
    private GiftType giftType;
    private String winnerMessage;
    private String code;
    private BroadcastType isPublic;
    private Integer questionCount;
    private Long createdTime;
    private Long updatedTime;
    private Long scheduledTime;

}
