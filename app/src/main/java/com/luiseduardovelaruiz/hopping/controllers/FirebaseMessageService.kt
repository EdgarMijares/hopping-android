package com.luiseduardovelaruiz.hopping.controllers

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.luiseduardovelaruiz.hopping.MenuActivity
import com.luiseduardovelaruiz.hopping.R


open class FirebaseMessageService : FirebaseMessagingService() {

    private var TAG = "SERVICIO_FIREBASE"

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Log.w("TokenID", p0)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        if (remoteMessage.notification != null) {
            mostrarNotificacion(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun mostrarNotificacion(title: String?, body: String){
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent: Intent = Intent(this, MenuActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificacion: Notification.Builder? = Notification.Builder(this)
                .setSmallIcon(R.drawable.icon).
                setContentTitle(title).
                setContentText(body).
                setAutoCancel(true).
                setSound(soundUri).
                setContentIntent(pendingIntent)

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificacion!!.build())
    }
}
