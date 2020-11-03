package com.lig.projemanage.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lig.projemanage.R
import com.lig.projemanage.activities.MainActivity
import com.lig.projemanage.activities.SignInActivity

class MyFirebaseMessagingService: FirebaseMessagingService() {
    private val TAG = this.javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "From ${remoteMessage.data}")
            val title = remoteMessage.data[com.lig.projemanage.utils.Constants.FCM_KEY_TITLE]!!
            val message = remoteMessage.data[com.lig.projemanage.utils.Constants.FCM_KEY_MESSAGE]!!
            sendNotification(title, message)
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "MessageNotification Body: ${it.body}")
        }
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e(TAG, "refresh Token : $token")
        sendRegistrationToServer(token)
    }


    private fun sendRegistrationToServer(token: String?){

    }

    private fun sendNotification(title: String, messageBody: String){
        val intent = if (FireStoreClass().getCurrentUserId().isNotEmpty()){ // check if user is log in
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, SignInActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TOP) // this prevent to create new instance
        //  les PendingIntent sont utilisés pour les notifications.
        //  Tu prépares un Intent dans un PendingIntent que tu attaches à une notification
        //  pour qu'il soit déclenché lorsque l'utilisateur cliquera sur ta notification.
        val pendingIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = this.resources.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificatioBuilder = NotificationCompat.Builder(this,channelId)
            .setSmallIcon(R.drawable.ic_stat_ic_notification)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.i(TAG, "Json sending notification ")
            val channel = NotificationChannel(channelId, "channel projectManag title", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificatioBuilder.build())
    }



}