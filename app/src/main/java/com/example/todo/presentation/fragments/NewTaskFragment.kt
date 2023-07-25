package com.example.todo.presentation.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.todo.model.TaskInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todo.R
import com.example.todo.databinding.FragmentNewTaskBinding
import com.example.todo.presentation.MainActivity
import com.example.todo.util.*
import com.example.todo.viewmodels.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class NewTaskFragment:Fragment()
{
    private lateinit var binding:FragmentNewTaskBinding
    private val taskViewModel by activityViewModels<TaskViewModel>()

    private val args by navArgs<NewTaskFragmentArgs>()
    private val newTaskArg by lazy { args.newTaskArg }

    private var taskInfo=TaskInfo(0,"","", Date(MAX_TIMESTAMP),false)

    override fun onCreateView(inflater:LayoutInflater, container:ViewGroup?, savedInstanceState:Bundle?):View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_new_task, container, false)
        binding.fragment=this
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        if(newTaskArg != null)
        {
            initUpdate()
            binding.taskDeleteIv.visibility=View.VISIBLE
        }
        setInitialValues()
    }

    private fun initUpdate()
    {
        taskInfo =newTaskArg!!
        binding.fab.contentDescription="update"
        binding.newTask.text="Edit Task"
    }

    private fun setInitialValues() {
        var str = DateToString.convertDateToString(taskInfo.date)
        if(str=="N/A")str="Due Date"

        binding.apply {
            titleEt.setText(taskInfo.title)
            descriptionEt.setText(taskInfo.description)
            dateAndTimePicker.text = str
            isCompleted.isChecked = taskInfo.status


            //ClickListeners
            dateAndTimePicker.setOnClickListener { showDateTimePicker()}
            isCompleted.setOnCheckedChangeListener{_,it-> taskInfo.status = it }
            fab.setOnClickListener{ addTask()}


        }
    }

    private fun showDateTimePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker().build()
        val timePicker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            taskInfo.date = calendar.time
            binding.dateAndTimePicker.text = DateToString.convertDateToString(taskInfo.date)
            timePicker.show(childFragmentManager, "TAG")
        }

        timePicker.addOnPositiveButtonClickListener{
            val cal = Calendar.getInstance()
            cal.time = taskInfo.date
            cal.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            cal.set(Calendar.MINUTE, timePicker.minute)
            cal.set(Calendar.SECOND, 5)
            taskInfo.date = cal.time
            binding.dateAndTimePicker.text = DateToString.convertDateToString(taskInfo.date)
        }
        datePicker.show(childFragmentManager,"TAG")
    }

    private fun addTask() {
        val date = Date()
        taskInfo.title=binding.titleEt.text.toString()
        taskInfo.description = binding.descriptionEt.text.toString()

        if(taskInfo.description.isEmpty() or taskInfo.title.isEmpty())
        {
            Toast.makeText(requireContext(), "Title or description cannot be empty", Toast.LENGTH_SHORT).show()
        }
        else
        {
            if(binding.fab.contentDescription=="update")
            {
                updateTask()
            }
            else
            {
                val diff = (Date().time/1000) - sDate
                taskInfo.id = diff.toInt()
                taskViewModel.addTask(taskInfo)
                if(!taskInfo.status && taskInfo.date>date && taskInfo.date.seconds == 5)
                {
                    AlarmHelper.setAlarm(requireContext(),taskInfo)
                }

            }

            findNavController().popBackStack()
        }
    }

    private fun updateTask()
    {
        val date = Date()
        taskViewModel.updateTask(taskInfo)

        if(!taskInfo.status && taskInfo.date> date && taskInfo.date.seconds == 5)
        {
            AlarmHelper.setAlarm(requireContext(),taskInfo)
        }
        else
        {

            AlarmHelper.cancelAlarm(requireContext(),taskInfo)
        }
    }

    private fun deleteTask()
    {
        taskViewModel.deleteTask(taskInfo)

        AlarmHelper.cancelAlarm(requireContext(),taskInfo)

        Toast.makeText(requireContext(), "Task deleted successfully", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
        
    }

    fun deleteCurrentTask()
    {
        AlertDialog.Builder(requireContext()).setTitle("Delete Task?")
            .setMessage("This task will be deleted")
            .setPositiveButton("Delete") {_, _ -> deleteTask()}
            .setNegativeButton("Cancel") {_, _ ->}
            .show()
    }

}