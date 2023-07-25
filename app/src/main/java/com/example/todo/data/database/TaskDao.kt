package com.example.todo.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todo.model.TaskInfo
import java.util.*

@Dao
interface TaskDao
{
    @Insert
    suspend fun insertTask(task :TaskInfo) : Long

    @Update
    suspend fun updateTaskStatus(task: TaskInfo) : Int

    @Delete
    suspend fun deleteTask(task: TaskInfo)

    @Query("SELECT * FROM taskInfo")
    fun getTasks(): LiveData<List<TaskInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "ORDER BY date")
    fun getUncompletedTask(): LiveData<List<TaskInfo>>

    @Transaction
    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 1 " +
            "ORDER BY date")
    fun getCompletedTask(): LiveData<List<TaskInfo>>

    @Query("SELECT * " +
            "FROM taskInfo " +
            "WHERE status = 0 " +
            "AND date > :currentTime")
    fun getActiveAlarms(currentTime :Date) : List<TaskInfo>
}