package com.example.todo.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieDrawable
import com.example.todo.R
import com.example.todo.databinding.FragmentHomeBinding
import com.example.todo.model.TaskInfo
import com.example.todo.presentation.adapter.CompletedTaskAdapter
import com.example.todo.presentation.adapter.TaskAdapter
import com.example.todo.util.AlarmHelper
import com.example.todo.viewmodels.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment()
{

    private lateinit var binding:FragmentHomeBinding
    private val taskViewModel by activityViewModels<TaskViewModel>()

    @Inject
    lateinit var taskAdapter:TaskAdapter

    @Inject
    lateinit var completedTaskAdapter:CompletedTaskAdapter

    override fun onCreate(savedInstanceState:Bundle?)
    {
        super.onCreate(savedInstanceState)
        taskViewModel.getCompletedTask()
        taskViewModel.getUncompletedTask()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        binding.fragment=this
        observeTaskLiveData()
        initRecyclerView()
        initRecyclerView2()
        return binding.root
    }

    override fun onViewCreated(view:View, savedInstanceState:Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        setItemClickListener()

    }

    private fun editTaskInformation(taskInfo:TaskInfo) {
        val action = HomeFragmentDirections.actionHomeFragmentToNewTaskFragment(taskInfo)
        findNavController().navigate(action)
    }

    private fun updateTaskStatus(taskInfo: TaskInfo)
    {
        taskViewModel.updateTask(taskInfo)
        if(taskInfo.status)
        {
            AlarmHelper.cancelAlarm(requireContext(),taskInfo)
        }
        else
        {
            val date = Date()
            if(taskInfo.date > date && taskInfo.date.seconds == 5){
                AlarmHelper.setAlarm(requireContext(),taskInfo)
            }
        }
    }

    private fun observeTaskLiveData()
    {
        taskViewModel.completedTaskLiveData.observe(viewLifecycleOwner)
        {

            if(it.isEmpty())
            {
                hideRecyclerView2()
            }
            else
            {
                showRecyclerView2()
                completedTaskAdapter.differ.submitList(it)
            }
        }

        taskViewModel.uncompletedTaskLiveData.observe(viewLifecycleOwner)
        {

            if(it.isEmpty())
            {
                hideRecyclerView1()
            }
            else
            {
                showRecyclerView1()
                taskAdapter.differ.submitList(it)
            }
        }
    }

    private fun initRecyclerView()
    {
        binding.tasksRecyclerView.adapter=taskAdapter
    }

    private fun initRecyclerView2()
    {
        binding.taskCompletedRecyclerView.adapter=completedTaskAdapter
    }

    fun addTask()
    {
        val action=HomeFragmentDirections.actionHomeFragmentToNewTaskFragment(null)
        findNavController().navigate(action)
    }

    private fun showRecyclerView2()
    {
        binding.apply {
            taskCompletedRecyclerView.visibility=View.VISIBLE
            taskCompleted.visibility=View.VISIBLE
        }
    }

    private fun hideRecyclerView2()
    {
        binding.apply {
            taskCompletedRecyclerView.visibility=View.GONE
            taskCompleted.visibility=View.GONE
        }
    }

    private fun showRecyclerView1()
    {
        binding.apply {
            tasksRecyclerView.visibility=View.VISIBLE
            animationViewPage.pauseAnimation()
            emptyTaskLL.visibility=View.GONE
        }
    }

    private fun hideRecyclerView1()
    {
        binding.apply {
            tasksRecyclerView.visibility=View.GONE
            animationViewPage.playAnimation()
            animationViewPage.repeatCount=LottieDrawable.INFINITE
            emptyTaskLL.visibility=View.VISIBLE
        }
    }

    private fun setItemClickListener()
    {
        taskAdapter.setOnItemClickListener {
            editTaskInformation(it)
        }

        taskAdapter.setOnTaskStatusChangedListener {
            updateTaskStatus(it)
        }

        completedTaskAdapter.setOnItemClickListener {
            editTaskInformation(it)
        }

        completedTaskAdapter.setOnTaskStatusChangedListener {
            updateTaskStatus(it)
        }
    }

}