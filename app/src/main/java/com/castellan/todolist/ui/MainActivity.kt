package com.castellan.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.*
import com.castellan.todolist.databinding.ActivityMainBinding
import com.castellan.todolist.datasource.TaskDataSource
import com.castellan.todolist.ui.adapter.TaskListAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.rvTasks.adapter = adapter
        updateList()

        setupAdapter()
        insertListeners()
    }

    private fun setupAdapter() {


        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, EDIT_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.delete(it)
            updateList()
        }

        adapter.listenerDone = {
            it.done()
            updateList()
        }
    }

    private fun insertListeners() {
        binding.fabAddTask.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == CREATE_NEW_TASK || requestCode == EDIT_TASK) && resultCode == Activity.RESULT_OK) updateList()

    }

    private fun updateList() {
        val list = TaskDataSource.get();
        binding.emptyInclude.emptyState.visibility = if (list.isEmpty()) VISIBLE else GONE
        binding.rvTasks.visibility = if (list.isEmpty()) GONE else VISIBLE

        adapter.submitList(list)
    }

    companion object {
        private val CREATE_NEW_TASK = 200
        private val EDIT_TASK = 300
    }
}