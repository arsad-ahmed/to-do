package com.example.todo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todo.model.TaskInfo
import com.example.todo.util.receivers.AlarmReceiver

object AlarmHelper {
    fun setAlarm(context:Context,taskInfo:TaskInfo) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra(TASK_INFO, taskInfo)
            PendingIntent.getBroadcast(context, taskInfo.id, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, taskInfo.date.time, alarmIntent)
    }

    fun cancelAlarm(context: Context, taskInfo:TaskInfo) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, taskInfo.id, alarmIntent, PendingIntent.FLAG_IMMUTABLE)
        pendingIntent?.let {
            alarmManager.cancel(it)
            it.cancel()
        }
    }
}