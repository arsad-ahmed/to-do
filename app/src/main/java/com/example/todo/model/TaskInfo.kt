package com.example.todo.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*


@Entity(tableName = "taskInfo")
@Parcelize
data class TaskInfo(
    @PrimaryKey(autoGenerate = false)
    var id : Int,
    var title:String,
    var description : String,
    var date :Date,
    var status : Boolean):Parcelable
