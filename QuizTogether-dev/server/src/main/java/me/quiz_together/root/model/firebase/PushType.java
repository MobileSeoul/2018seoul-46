package me.quiz_together.root.model.firebase;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PushType {
    CHAT_MESSAGE,
    ADMIN_MESSAGE,
    QUESTION_MESSAGE,
    ANSWER_MESSAGE,
    WINNERS_MESSAGE,
    NOTICE_MESSAGE,
    END_MESSAGE,
    FOLLOW_BROADCAST,
    BROADCAST_PLAY_INFO;
}
