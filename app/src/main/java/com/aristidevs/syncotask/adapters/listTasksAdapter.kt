package com.aristidevs.syncotask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.objects.Task

class listTasksAdapter(private val listTasks: List<Task>): RecyclerView.Adapter<listTasksAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_list_tasks, viewGroup, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val item = listTasks[i]
        viewHolder.render(item)
    }

    override fun getItemCount(): Int = listTasks.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val title = itemView.findViewById<TextView>(R.id.cardList_title)
        val priotiry = itemView.findViewById<TextView>(R.id.cardList_priority)
        val time = itemView.findViewById<TextView>(R.id.cardList_time)
        val date = itemView.findViewById<TextView>(R.id.cardList_date)

        fun render(task: Task){
            title.text = task.title
            priotiry.text = task.priority
            time.text = task.startTime + " : " + task.endTime
            date.text = task.date
        }
    }
}