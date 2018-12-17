package com.quiz_together.data.model

enum class GiftType(val value:Int) {
    PRIZE(100),
    GIFT(200)
}

enum class BroadcastType (val value:Int) {
    PUBLIC(100),
    PRIVATE(200);
}

enum class CategoryType (val value:Int){
    NORMAL(0);
}

enum class UserStatus(val value:Int = 0) {
    NORMAL(0),
    DELETED(1)
}

enum class PlayUserStatus(val value:Int) {
    PLAY(100),
    LOSER(200),
    VIEWER(300)
}

enum class BroadcastStatus(val value:Int) {
    CREATED(100),
    WATING(200),
    OPEN_QUESTION(300),
    OPEN_ANSWER(400)
}

enum class PushType(val value:Int) {
    CHAT_MESSAGE(100),
    ADMIN_MESSAGE(200),
    QUESTION_MESSAGE(300),
    ANSWER_MESSAGE(400),
    WINNERS_MESSAGE(500),
    NOTICE_MESSAGE(600),
    END_MESSAGE(700),
    FOLLOW_BROADCAST(800)
}

enum class RoomOutputType(value: Int) {
    DEFAULT(100),
    FOLLOW(200),
    RESERVATION(300),
}
