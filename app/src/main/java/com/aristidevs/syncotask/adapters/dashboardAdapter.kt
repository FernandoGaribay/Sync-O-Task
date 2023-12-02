package com.aristidevs.syncotask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.Task

class dashboardAdapter(private var listTasks: List<Task>): RecyclerView.Adapter<dashboardAdapter.ViewHolder>() {

    private lateinit var clickListener: onTaskClickListener
    private val VIEW_TYPE_WITH_TASKS = 1
    private val VIEW_TYPE_NO_TASKS = 2

    fun setOnTaskClickListener(listener: onTaskClickListener) {
        this.clickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WITH_TASKS -> {
                val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_dashboard_task, viewGroup, false)
                ViewHolder(v)
            }
            VIEW_TYPE_NO_TASKS -> {
                val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_dashboard_notask, viewGroup, false)
                ViewHolder(v)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (listTasks.isNotEmpty()) {
            val item = listTasks[i]
            viewHolder.render(item)
        }
    }

    override fun getItemCount(): Int {
        return if (listTasks.isEmpty()) 1 else listTasks.size
    }

    // Definir el tipo de vista
    override fun getItemViewType(position: Int): Int {
        return if (listTasks.isEmpty()) VIEW_TYPE_NO_TASKS else VIEW_TYPE_WITH_TASKS
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val cardView = itemView.findViewById<CardView>(R.id.card_view)
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
                // Verifica si la vista es de tipo VIEW_TYPE_WITH_TASKS antes de intentar obtener la tarea
                if (getItemViewType(position) == VIEW_TYPE_WITH_TASKS) {
                    val clickedTask = listTasks[position]
                    clickListener.onItemClick(clickedTask)
                }
            }
        }

        fun render(task: Task) {
            title.text = task.title
            priotiry.text = task.priority
            description.text = task.description
            startTime.text = task.startTime
            date.text = task.date

            // Cambia el color de fondo del CardView segun la prioridad
            when (task.priority) {
                "Low Priority" -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.light_green))
                "Medium Priority" -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.light_yellow))
                "High Priority" -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.light_red))
                "Max Priority" -> cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.light_purple))
            }
        }
    }
}