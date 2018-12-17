package com.quiz_together.data.remote

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.quiz_together.R
import com.quiz_together.data.model.PushType
import com.quiz_together.ui.main.MainActivity


class FirebaseHelper : FirebaseMessagingService() {

    val TAG = "MyFirebaseMessagi...#$#"

    override fun onNewToken(newToken: String?) {
        super.onNewToken(newToken)

        Log.i(TAG,"newToken : $newToken")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {

            val gsObj = JsonObject()
            val jsonParser = JsonParser()
            val map = remoteMessage.data
            var str: String

            for (key in map.keys) {
                str = map[key]!!
                try {
                    gsObj.add(key, jsonParser.parse(str))
                } catch (e: Exception) {
                    gsObj.addProperty(key, str)
                }
            }

            Log.i(TAG, "## GET MSG FROM FIREBASE >> " + gsObj.toString())

            if (gsObj.get("pushType").asString == PushType.FOLLOW_BROADCAST.name) {

                sendNotification(title = gsObj.get("title").asString, description = gsObj.get("description").asString,
                        broadcastId =  gsObj.get("broadcastId").asString,userName =  gsObj.get("userName").asString)
                return
            }

            val intent = Intent(FMC_ACTION)
            intent.putExtra(FMC_IN_QUIZING, gsObj.toString())
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    fun sendNotification(title: String, broadcastId: String, userName: String , description: String) {

        Log.i(TAG,"sendNotification : $title $broadcastId $userName $description")



        var channelId = "channel"
        var channelName = "Channel Name"

        var notifManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notifManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.putExtra(MainActivity.BROADCAST_ID, broadcastId)
        notificationIntent.putExtra(MainActivity.USER_ID, userName)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val requestID = System.currentTimeMillis().toInt()
        val pendingIntent = PendingIntent.getActivity(applicationContext, requestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)




        /////



        builder.setContentTitle(title)
                .setContentText(description)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSound(RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(android.R.drawable.btn_star) // TODO 로고 백터이미지
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.qx_icon_512))
                .setBadgeIconType(R.drawable.icc_add)
                .setContentIntent(pendingIntent)

        notifManager.notify(0, builder.build())
    }


    companion object {
        const val FMC_IN_QUIZING = "FMC_IN_QUIZING"
        const val FMC_ACTION = "FMC_ACTION"

    }


}