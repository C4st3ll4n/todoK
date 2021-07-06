package com.castellan.todolist.ui.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.castellan.todolist.R
import com.castellan.todolist.R.color.purple_700
import com.castellan.todolist.databinding.ItemTaskBinding
import com.castellan.todolist.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}
    var listenerDone: (Task) -> Unit = {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task?) {
            binding.tvTaskTitle.text = item?.title
            binding.tvTaskDate.text = "${item?.date} ${item?.hour}"
            binding.ivTaskPopup.setOnClickListener { showPopUp(item!!) }

            if (item?.done == true){
                binding.tvTaskTitle.setTextColor(Color.GREEN)
            }
        }

        private fun showPopUp(item: Task){
            val ivMore = binding.ivTaskPopup
            val popupMenu = PopupMenu(ivMore.context, ivMore)
            if(!item.done) popupMenu.menu.removeItem(R.id.action_done)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId){
                    R.id.action_delete -> listenerDelete(item)
                    R.id.action_edit -> listenerEdit(item)
                    R.id.action_done -> listenerDone(item)
                }
                return@setOnMenuItemClickListener true
            }

            popupMenu.show()
        }

    }

}

class DiffCallback: DiffUtil.ItemCallback<Task>(){
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean  = oldItem.id == newItem.id
}