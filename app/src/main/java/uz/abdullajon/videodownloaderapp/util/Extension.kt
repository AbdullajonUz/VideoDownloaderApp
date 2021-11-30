package uz.abdullajon.videodownloaderapp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import uz.abdullajon.videodownloaderapp.R
import uz.abdullajon.videodownloaderapp.data.model.VideoDownloadModel
import uz.abdullajon.videodownloaderapp.ui.MainActivity
import uz.perfectalgorithm.projects.tezkor.utils.coroutinescope.CoroutinesScope

fun Fragment.showToast(string: String) {
    Toast.makeText(requireContext(), "$string", Toast.LENGTH_SHORT).show()
}

fun Context.setNotificationVideo(url: String, videoDownloadModel: VideoDownloadModel) {

    val pendingIntent = getPendingIntent(this)

    CoroutinesScope.io {
        val channelId =
            "notifcation_chat_${videoDownloadModel.id}"
        val notification = getNotification(pendingIntent, channelId, url)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                url,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        try {
            notificationManager.notify(videoDownloadModel.id, notification)
        } catch (e: Exception) {

        }
    }
}


private fun Context.getNotification(
    pendingIntent: PendingIntent,
    channelId: String,
    url: String
): Notification {


    val builder = NotificationCompat.Builder(this, channelId)
        .setContentTitle(url)
        .setSmallIcon(R.drawable.ic_download)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_MAX)
        .setCategory(Notification.CATEGORY_EVENT)
        .setAutoCancel(true)

    return builder.build()
}

private fun getPendingIntent(
    context: Context,
): PendingIntent {

    return NavDeepLinkBuilder(context)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.mobile_navigation)
        .setDestination(R.id.nav_home)
        .createPendingIntent()

}