package com.castellan.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.castellan.todolist.databinding.ActivityAddTaskBinding
import com.castellan.todolist.datasource.TaskDataSource
import com.castellan.todolist.extensions.format
import com.castellan.todolist.extensions.text
import com.castellan.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private val TAG = "AddTaskActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(TASK_ID)){
            val itemId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(itemId)?.let {
                bindTask(it)
            }
        }

        inserListeners()
    }

    private fun bindTask(it: Task) {
        binding.tilHour.text = it.hour
        binding.tilDate.text = it.date
        binding.tilTitle.text = it.title
        binding.tilDesc.text = it.description
    }

    private fun inserListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timezone = TimeZone.getDefault()
                val offset = timezone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if(timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if(timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, "TIME_PICKER_TAG")
        }

        binding.btnNewTask.setOnClickListener {
            val task = Task(
                title = binding.tilTitle.text,
                description = binding.tilDesc.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                done = false,
                id= intent.getIntExtra(TASK_ID, 0)
            )

            TaskDataSource.add(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    companion object{
        const val TASK_ID = "task_id"
    }
}