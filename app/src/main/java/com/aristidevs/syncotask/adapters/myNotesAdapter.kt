package com.aristidevs.syncotask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.interfaces.onNoteClickListener
import com.aristidevs.syncotask.objects.Note

class myNotesAdapter (private var listNotes: List<Note>): RecyclerView.Adapter<myNotesAdapter.ViewHolder>() {

    private lateinit var clickListener: onNoteClickListener
    private val VIEW_TYPE_WITH_NOTES = 1
    private val VIEW_TYPE_NO_NOTES = 2

    fun setOnNoteClickListener(listener: onNoteClickListener) {
        this.clickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_WITH_NOTES -> {
                val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_my_notes_note, viewGroup, false)
                ViewHolder(v)
            }
            VIEW_TYPE_NO_NOTES -> {
                val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_my_notes_nonotes, viewGroup, false)
                ViewHolder(v)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        if (listNotes.isNotEmpty()) {
            val item = listNotes[i]
            viewHolder.render(item)
        }
    }

    override fun getItemCount(): Int {
        return if (listNotes.isEmpty()) 1 else listNotes.size
    }

    // Definir el tipo de vista
    override fun getItemViewType(position: Int): Int {
        return if (listNotes.isEmpty()) VIEW_TYPE_NO_NOTES else VIEW_TYPE_WITH_NOTES
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val cardView = itemView.findViewById<CardView>(R.id.card_view)
        val title = itemView.findViewById<TextView>(R.id.card_title)
        val description = itemView.findViewById<TextView>(R.id.card_description)
        val date = itemView.findViewById<TextView>(R.id.card_date)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // Verifica si la vista es de tipo VIEW_TYPE_WITH_TASKS antes de intentar obtener la tarea
                if (getItemViewType(position) == VIEW_TYPE_WITH_NOTES) {
                    val clickedTask = listNotes[position]
                    clickListener.onItemClick(clickedTask)
                }
            }
        }

        fun render(note: Note) {
            title.text = note.title
            description.text = note.description
            date.text = note.date
        }
    }
}