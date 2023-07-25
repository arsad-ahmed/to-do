package com.example.todo.util.receivers


import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todo.R
import com.example.todo.model.TaskInfo
import com.example.todo.presentation.MainActivity
import com.example.todo.util.TASK_INFO
import com.example.todo.util.TASK_REMINDER
import com.example.todo.util.TO_DO_LIST

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context:Context, intent:Intent) {

        val taskInfo=intent.getParcelableExtra<Parcelable>(TASK_INFO) as TaskInfo
        if (taskInfo.id != -1)
        {
            showNotification(context,taskInfo)
        }
    }


    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, alarmId: TaskInfo)
    {
        val notificationId = alarmId.id

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, notificationId, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(context, TO_DO_LIST)
            .setContentTitle(TASK_REMINDER)
            .setContentText("${alarmId.description}")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, notificationBuilder.build())
        }
    }
}