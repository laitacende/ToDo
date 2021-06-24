package com.example.todo2.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo2.*
import com.example.todo2.R
import com.example.todo2.databinding.ActivityMainBinding
import com.example.todo2.enums.Category
import com.example.todo2.enums.Priorities
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var tasks : MutableList<Task> = ArrayList()
    var adapter : CustomAdapter? = null
    var typeSort = "Date"
    val STATE_KEY = "state_key"
    val LIST_KEY = "state_key_list"
    var layoutState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        adapter = CustomAdapter(tasks) { pos: Int ->  editItem(pos) }
        binding.tasks.adapter = adapter
        setContentView(view)
        binding.tasks.layoutManager = LinearLayoutManager(this)
        binding.tasks.addOnScrollListener( object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    binding.addNew.hide()
                } else if (dy <= 0) {
                    binding.addNew.show()
                }
            }
        })
        val itemTouchHelperCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        adapter?.removeItem(this@MainActivity, viewHolder as CustomAdapter.ViewHolder)
                        adapter?.notifyItemChanged(viewHolder.adapterPosition)
                }
            }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.tasks)
        ArrayAdapter.createFromResource(this, R.array.sort, R.layout.spinner_sort_selected)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(R.layout.spinner_item)
                // Apply the adapter to the spinner
                binding.sort.adapter = adapter
            }
        binding.sort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                    arg0: AdapterView<*>?,
                    arg1: View?,
                    position: Int,
                    id: Long
            ) {
                typeSort = binding.sort.selectedItem.toString()
                sort(typeSort)
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
    }

    fun addItem(view: View) {
        val intent = Intent(this, AddActivity::class.java)
        startActivityForResult(intent, 100)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putParcelableArrayList(LIST_KEY, tasks as java.util.ArrayList<out Parcelable>)
        layoutState = binding.tasks.layoutManager?.onSaveInstanceState()
        binding.tasks.layoutManager = LinearLayoutManager(this)
        savedInstanceState.putParcelable(STATE_KEY, layoutState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        tasks = savedInstanceState.getParcelableArrayList<Task>(LIST_KEY) as ArrayList<Task>
        layoutState = savedInstanceState.getParcelable(STATE_KEY)
        binding.tasks.layoutManager = LinearLayoutManager(this)
        binding.tasks.layoutManager?.onRestoreInstanceState(layoutState)
        adapter = CustomAdapter(tasks) { pos: Int ->  editItem(pos)}
        binding.tasks.adapter = adapter
    }

    private fun editItem(pos: Int) {
        val intent = Intent(this, EditActivity::class.java);
        intent.putExtra("title", tasks[pos].title)
        intent.putExtra("des", tasks[pos].description)
        intent.putExtra("date", tasks[pos].date.toString())
        intent.putExtra("time", tasks[pos].time.toString())
        intent.putExtra("category", tasks[pos].category.toString())
        intent.putExtra("priority", tasks[pos].priority.toString())
        intent.putExtra("pos", pos)
        startActivityForResult(intent, 200);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 || requestCode == 200) {
            val taskName = data?.getStringExtra("name")
            val taskDes = data?.getStringExtra("des")
            val taskCat = data?.getStringExtra("category")
            val taskPrio = data?.getStringExtra("priority")
            val taskDate = data?.getStringExtra("date")
            val taskTime = data?.getStringExtra("time")
            if (taskName != null && taskDes != null && taskCat != null && taskPrio != null
                    && taskDate != null && taskTime != null) {
                if (requestCode == 100) {
                    val newTask = Task(taskName, taskDes, LocalDate.parse(taskDate), Category.valueOf(taskCat),
                            LocalTime.parse(taskTime, DateTimeFormatter.ISO_TIME), Priorities.valueOf(taskPrio), false)
                    tasks.add(newTask)
                    sort(typeSort)
                    adapter?.notifyDataSetChanged()
                } else {
                    // edit item
                    val pos = data?.getIntExtra("pos", -1)
                    if (pos != -1) {
                        tasks[pos].title = taskName
                        tasks[pos].description = taskDes
                        tasks[pos].date = LocalDate.parse(taskDate)
                        tasks[pos].category = Category.valueOf(taskCat)
                        tasks[pos].time = LocalTime.parse(taskTime, DateTimeFormatter.ISO_TIME)
                        tasks[pos].priority = Priorities.valueOf(taskPrio)
                        sort(typeSort)
                        adapter?.notifyItemChanged(pos)
                    }
               }
            }
        }
    }

    private fun sort(type: String) {
        when {
            binding.sort.selectedItem.toString().equals("Name") -> {
                tasks.sortBy { it.title }
                adapter?.notifyDataSetChanged()
            }
            binding.sort.selectedItem.toString().equals("Category") -> {
                tasks.sortBy { it.category }
                adapter?.notifyDataSetChanged()
            }
            binding.sort.selectedItem.toString().equals("Date") -> {
                tasks.sortBy { it.date }
                adapter?.notifyDataSetChanged()
            }
            binding.sort.selectedItem.toString().equals("Priority") -> {
                tasks.sortBy { it.priority }
                adapter?.notifyDataSetChanged()
            }
            binding.sort.selectedItem.toString().equals("Status") -> {
                tasks.sortBy { it.done }
                adapter?.notifyDataSetChanged()
            }
        }
    }
}