package com.example.todo2

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo2.activities.MainActivity
import com.example.todo2.enums.Category
import com.example.todo2.enums.Priorities
import org.jetbrains.anko.doAsync
import java.time.format.DateTimeFormatter

class CustomAdapter(private val data: MutableList<Task>,
                    val editItem: ((pos: Int ) -> Unit)? = null) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleView: TextView
        var descriptionView: TextView
        val dateView: TextView
        val timeView: TextView
        val image: ImageView
        val checkBox: CheckBox

        init {
            titleView = view.findViewById(R.id.taskItem)
            dateView = view.findViewById(R.id.date)
            timeView = view.findViewById(R.id.time)
            descriptionView = view.findViewById(R.id.des)
            descriptionView.visibility = View.GONE
            image = view.findViewById(R.id.cat)
            checkBox = view.findViewById(R.id.done)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener {
            if (viewHolder.descriptionView.visibility == View.GONE) {
                if (viewHolder.descriptionView.length() != 0) {
                    viewHolder.descriptionView.visibility = View.VISIBLE
                }
            } else {
                viewHolder.descriptionView.visibility = View.GONE
            }
        }

        view.setOnLongClickListener {
            this@CustomAdapter.editItem?.invoke(viewHolder.adapterPosition)
            return@setOnLongClickListener true
        }

        /*viewHolder.button.setOnClickListener {
            this@CustomAdapter.editItem?.invoke(viewHolder.adapterPosition)
        }*/

        viewHolder.checkBox.setOnClickListener {
            data[viewHolder.adapterPosition].done =  viewHolder.checkBox.isChecked
            when {
                viewHolder.checkBox.isChecked -> {
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#f7f7f7"))
                }
                data[viewHolder.adapterPosition].priority == Priorities.IMPORTANT -> {
                    viewHolder.itemView.setBackgroundColor(Color.parseColor("#ffe6e6"))
                }
                else -> {
                    viewHolder.itemView.setBackgroundColor(Color.WHITE)
                }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleView.text = data[position].title
        holder.dateView.text = data[position].date.toString()
        holder.timeView.text = data[position].time.format(DateTimeFormatter.ofPattern("HH:mm")).toString()
        holder.descriptionView.text = data[position].description
        holder.checkBox.isChecked = data[position].done
        /*holder.checkBox.setOnClickListener {
            data[position].done =  holder.checkBox.isChecked
            doAsync {  taskDao?.update(data[position]) }
        }*/

        when {
            data[position].done -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#f7f7f7"))
            }
            data[position].priority == Priorities.IMPORTANT -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#ffe6e6"))
            }
            data[position].priority == Priorities.NORMAL -> {
                holder.itemView.setBackgroundColor(Color.WHITE)
            }
        }

        when (data[position].category) {
            Category.HOME -> {
                holder.image.setImageResource(R.drawable.home_range)
            }
            Category.WORK -> {
                holder.image.setImageResource(R.drawable.work)
            }
            Category.SCHOOL -> {
                holder.image.setImageResource(R.drawable.school)
            }
            Category.SHOPPING -> {
                holder.image.setImageResource(R.drawable.shopping)
            }
            Category.OTHERS -> {
                holder.image.setImageResource(R.drawable.others)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun removeItem(activity: MainActivity, holder: ViewHolder) {
        val builderDialog = DialogDeletion()
        val dialog = builderDialog.createDialog(activity, this, holder.adapterPosition, data)
        dialog?.show()
    }
}
