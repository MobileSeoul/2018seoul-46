package com.quiz_together.data.model

data class AnswerMsg(val pushType: PushType, val questionProp: QuestionProp, val answerNo:Int, val step:Int, val questionStatistics:Map<String,Int>)

data class QuestionMsg(val pushType: PushType,val questionProp: QuestionProp, val step:Int )

data class ChatMsg( val pushType: PushType,val message:String, val userName:String)

data class AdminMsg(val pushType: PushType, val message:String, val userName:String)

data class WinnersMsg(val pushType: PushType,val winnerMessage:String, val prize:String, val giftDescription:String, val giftType: GiftType, val userName:Array<String>)

//data class NoticeMsg(val pushType: PushType,val aa:Int)

data class EndMsg(val pushType: PushType)

data class ReqOpenAnsAndQus(val broadcastId: String, val userId: String, val step:Int)

data class FollowBroadcastMsg( val pushType: PushType, val broadcastId: String , val title:String, val description:String, val userName: String )


