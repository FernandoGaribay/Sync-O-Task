package com.aristidevs.syncotask.adapters

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aristidevs.syncotask.R
import com.aristidevs.syncotask.interfaces.onTaskClickListener
import com.aristidevs.syncotask.objects.Task

class listTasksAdapter(private var listTasks: List<Task>): RecyclerView.Adapter<listTasksAdapter.ViewHolder>() {

    private lateinit var clickListener: onTaskClickListener

    fun setOnTaskClickListener(listener: onTaskClickListener) {
        this.clickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_list_tasks, viewGroup, false)
        return ViewHolder(v);
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val item = listTasks[i]
        viewHolder.render(item)
    }

    override fun getItemCount(): Int = listTasks.size

    fun updateData(newList: List<Task>) {
        listTasks = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val title = itemView.findViewById<TextView>(R.id.cardList_title)
        val priotiry = itemView.findViewById<TextView>(R.id.cardList_priority)
        val bcPriotiry = itemView.findViewById<LinearLayout>(R.id.bcPriority)
        val imgPriority = itemView.findViewById<ImageView>(R.id.imgPriority)
        val time = itemView.findViewById<TextView>(R.id.cardList_time)
        val date = itemView.findViewById<TextView>(R.id.cardList_date)
        val layoutTags = itemView.findViewById<LinearLayout>(R.id.layoutTags)

        init {
            itemView.setOnClickListener(this)
        }

        fun render(task: Task){
            title.text = task.title
            priotiry.text = task.priority

            if(priotiry.text == "Max Priority"){
                bcPriotiry.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.purple))
                imgPriority.setImageResource(R.drawable.max)
            } else if (priotiry.text == "High Priority"){
                bcPriotiry.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.red))
                imgPriority.setImageResource(R.drawable.high)
            } else if (priotiry.text == "Medium Priority"){
                bcPriotiry.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.yellow))
                imgPriority.setImageResource(R.drawable.medium)
            } else {
                bcPriotiry.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.green))
                imgPriority.setImageResource(R.drawable.low)
            }

            if(!(task.tags.size == 0)){
                for (i in 0 until task.tags.size) {
                    addTag(task.tags[i])
                }
            } else {
                addTag("/*0No Tags/*0")
            }

            time.text = task.startTime + " : " + task.endTime
            date.text = task.date
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedTask = listTasks[position]
                clickListener.onItemClick(clickedTask)
            }
        }

        private fun addTag(tagName: String) {
            val newTag = createNewTag(tagName)
            layoutTags.addView(newTag)
        }

        private fun createNewTag(tagName: String): TextView {
            val newTag = TextView(itemView.context)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val marginRight = itemView.resources.getDimensionPixelSize(R.dimen.listTag_margin_right)
            params.setMargins(0, 0, marginRight, 0)
            newTag.setPadding(
                itemView.resources.getDimensionPixelSize(R.dimen.listTag_padding_left),
                itemView.resources.getDimensionPixelSize(R.dimen.listTag_padding_top),
                itemView.resources.getDimensionPixelSize(R.dimen.listTag_padding_right),
                itemView.resources.getDimensionPixelSize(R.dimen.listTag_padding_bottom)
            )

            newTag.layoutParams = params
            newTag.text = tagName
            newTag.setBackgroundResource(R.drawable.bc_tags)
            newTag.setTypeface(null, Typeface.BOLD)
            newTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            newTag.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))

            if(tagName == "/*0No Tags/*0"){
                newTag.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.light_gray))
                newTag.text = "TIP: Add tags for better organization."
            }

            return newTag
        }
    }
}