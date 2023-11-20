package com.aristidevs.syncotask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.Task

class dashboardAdapter(private var listTasks: List<Task>): RecyclerView.Adapter<dashboardAdapter.ViewHolder>() {

    private lateinit var clickListener: onTaskClickListener
    //val images = intArrayOf(R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_foreground)

    fun setOnTaskClickListener(listener: onTaskClickListener) {
        this.clickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_dashboard_task, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val item = listTasks[i]
        viewHolder.render(item)
    }

    // Regresa el tamaño de la lista ( para mostrar todas los elementos)
    override fun getItemCount(): Int = listTasks.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val title = itemView.findViewById<TextView>(R.id.card_title)
        val priotiry = itemView.findViewById<TextView>(R.id.card_priority)
        val description = itemView.findViewById<TextView>(R.id.card_description)
        val startTime = itemView.findViewById<TextView>(R.id.card_startTime)
        val date = itemView.findViewById<TextView>(R.id.card_date)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedTask = listTasks[position]
                clickListener.onItemClick(clickedTask)
            }
        }

        fun render(task: Task){
            title.text = task.title
            priotiry.text = task.priority
            description.text = task.description
            startTime.text = task.startTime
            date.text = task.date
        }
    }
}