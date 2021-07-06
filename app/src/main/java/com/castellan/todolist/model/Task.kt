package com.castellan.todolist.model

data class Task(
    val title: String,
    val description: String,
    val date: String, val hour: String,
    var id: Int = 0, var done: Boolean
) {

    fun done() {
        this.done = true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }


}
