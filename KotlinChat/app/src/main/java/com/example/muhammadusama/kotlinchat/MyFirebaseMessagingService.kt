package com.example.muhammadusama.kotlinchat

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)

        val title : String? = p0?.notification?.title
        val body : String? = p0?.notification?.body
        val clickAction : String? =  p0?.notification?.clickAction
        val recieverID : String? = p0?.data?.get("UserID")

        val mBuilder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)

        val resultIntent = Intent(clickAction)
        resultIntent.putExtra("UserID",recieverID)


        val mNotificationId = System.currentTimeMillis()
        val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(mNotificationId.toInt(),mBuilder.build())
    }
}