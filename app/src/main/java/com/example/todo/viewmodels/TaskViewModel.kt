package com.example.todo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.repository.TaskRepository
import com.example.todo.model.TaskInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository:TaskRepository):ViewModel()
{

    private lateinit var _completedTaskLiveData:LiveData<List<TaskInfo>>
    val completedTaskLiveData get() = _completedTaskLiveData

    private lateinit var _uncompletedTaskLiveData:LiveData<List<TaskInfo>>
    val uncompletedTaskLiveData get() = _uncompletedTaskLiveData

    fun addTask(taskInfo:TaskInfo)
    {
        viewModelScope.launch (Dispatchers.IO){
            taskRepository.addTask(taskInfo)
        }
    }

    fun deleteTask(taskInfo:TaskInfo)
    {
        viewModelScope.launch (Dispatchers.IO){
            taskRepository.deleteTask(taskInfo)
        }
    }

    fun updateTask(taskInfo:TaskInfo)
    {
        viewModelScope.launch (Dispatchers.IO){
            taskRepository.updateTask(taskInfo)
        }
    }

    fun getCompletedTask()
    {
        viewModelScope.launch (Dispatchers.IO){
            _completedTaskLiveData=taskRepository.getCompletedTask()
        }
    }

    fun getUncompletedTask()
    {
        viewModelScope.launch (Dispatchers.IO){
            _uncompletedTaskLiveData=taskRepository.getUncompletedTask()
        }
    }

}