package com.example.todo.data.repository

import androidx.lifecycle.LiveData
import com.example.todo.data.database.TaskDao
import com.example.todo.model.TaskInfo
import java.util.*
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao:TaskDao)
{
    suspend fun addTask(taskInfo:TaskInfo)
    {
        taskDao.insertTask(taskInfo)
    }
    suspend fun deleteTask(taskInfo:TaskInfo)
    {
        taskDao.deleteTask(taskInfo)
    }
    suspend fun updateTask(taskInfo:TaskInfo)
    {
        taskDao.updateTaskStatus(taskInfo)
    }

    suspend fun getCompletedTask():LiveData<List<TaskInfo>>
    {
        return taskDao.getCompletedTask()
    }

    suspend fun getUncompletedTask():LiveData<List<TaskInfo>>
    {
        return taskDao.getUncompletedTask()
    }

    suspend fun getActiveAlarms(currentTime:Date):List<TaskInfo>
    {
        return taskDao.getActiveAlarms(currentTime)
    }
}