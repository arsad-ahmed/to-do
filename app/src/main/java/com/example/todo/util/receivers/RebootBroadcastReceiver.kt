package com.example.todo.util.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.todo.data.repository.TaskRepository
import com.example.todo.util.AlarmHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class RebootBroadcastReceiver:BroadcastReceiver()
{
    @Inject
    lateinit var repository: TaskRepository

    override fun onReceive(context:Context?, intent:Intent?)
    {
        val time = Date()
        CoroutineScope(Main).launch {
            val list = repository.getActiveAlarms(time)
            for(taskInfo in list)
            {
                if(context != null)
                {
                    AlarmHelper.setAlarm(context, taskInfo)
                }
            }
        }
    }
}