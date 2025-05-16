package com.blogspot.carirunners.run.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.res.ResourcesCompat
import com.blogspot.carirunners.run.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber
import com.blogspot.carirunners.run.R as appR

/**
 * @author fabiolee
 */
class FcmMessagingService : FirebaseMessagingService() {
    companion object {
        private const val KEY_URL = "url"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Timber.d("From: " + remoteMessage.from)

        var url: String? = null
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Timber.d("Message data payload: " + remoteMessage.data)
            url = remoteMessage.data[KEY_URL]
        }

        // Check if message contains a notification payload.
        val messageNotification = remoteMessage.notification
        if (messageNotification != null) {
            Timber.d("Message Notification Body: " + messageNotification.body)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel()
            }
            sendNotification(messageNotification.body, url)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        if (notificationManager != null) {
            val channelName: CharSequence = "Run"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(
                getString(appR.string.fcm_notification_channel_id), channelName, importance
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(
                100,
                200,
                300,
                400,
                500,
                400,
                300,
                200,
                400
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(messageBody: String?, url: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(KEY_URL, url)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(
            this, getString(appR.string.fcm_notification_channel_id)
        )
            .setSmallIcon(appR.drawable.fcm_ic_directions_run_white_24dp)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    resources,
                    appR.mipmap.ic_launcher_round
                )
            )
            .setColor(ResourcesCompat.getColor(resources, appR.color.accent, theme))
            .setContentTitle(getString(appR.string.app_name))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager?.notify(0, notificationBuilder.build())
    }
}
