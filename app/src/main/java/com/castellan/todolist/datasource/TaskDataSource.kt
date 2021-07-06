package com.castellan.todolist.datasource

import com.castellan.todolist.model.Task

object TaskDataSource {
    private val list = arrayListOf<Task>()

    fun get() = list.toList()

    fun add(task: Task) {
        if(task.id==0) list.add(task.copy(id = list.size + 1))
        else {
            list.remove(task)
            list.add(task)
        }
    }

    fun findById(itemId: Int) =
        list.find { it.id == itemId }

    fun delete(it: Task) {
        list.remove(it)
    }

}