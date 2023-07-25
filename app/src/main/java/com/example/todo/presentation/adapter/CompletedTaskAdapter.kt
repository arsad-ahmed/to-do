package com.example.todo.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.databinding.TaskItemBinding
import com.example.todo.model.TaskInfo
import javax.inject.Inject

class CompletedTaskAdapter @Inject constructor():RecyclerView.Adapter<CompletedTaskAdapter.TaskViewHolder>()
{
    private val callback = object : DiffUtil.ItemCallback<TaskInfo>()
    {
        override fun areItemsTheSame(oldItem:TaskInfo, newItem:TaskInfo): Boolean
        {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem:TaskInfo, newItem:TaskInfo): Boolean
        {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int): TaskViewHolder
    {
        return TaskViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.task_item, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int)
    {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int
    {
        return differ.currentList.size
    }

    inner class TaskViewHolder(private val taskItemBinding:TaskItemBinding) : RecyclerView.ViewHolder(taskItemBinding.root)
    {
        fun bind(taskInfo:TaskInfo){
            taskItemBinding.taskInfo=taskInfo
            taskItemBinding.executePendingBindings()

            taskItemBinding.isCompleted.setOnCheckedChangeListener{_,it->
                taskInfo.status = it
                onTaskStatusChangedListener?.let {
                    it(taskInfo)
                }
            }

            taskItemBinding.root.setOnClickListener{
                onItemClickListener?.let {
                    it(taskInfo)
                }
            }

        }
    }

    private var onTaskStatusChangedListener :((TaskInfo)->Unit)?=null
    fun setOnTaskStatusChangedListener(listener :(TaskInfo)->Unit){
        onTaskStatusChangedListener = listener
    }

    private var onItemClickListener :((TaskInfo)->Unit)?=null
    fun setOnItemClickListener(listener :(TaskInfo)->Unit){
        onItemClickListener = listener
    }
}