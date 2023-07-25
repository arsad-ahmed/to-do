package com.example.todo.util

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.widget.CompoundButtonCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import java.util.*


@BindingAdapter("check_status")
fun setCheckStatus(materialCheckBox:MaterialCheckBox, status : Boolean){
    materialCheckBox.setOnCheckedChangeListener(null)
    materialCheckBox.isChecked = status
    CompoundButtonCompat.setButtonTintList(materialCheckBox, ColorStateList.valueOf(Color.RED))
}

@SuppressLint("SetTextI18n")
@BindingAdapter("set_date")
fun setDate(dueDate:MaterialTextView, date :Date){
    dueDate.text = "Due : " + DateToString.convertDateToString(date)
}

