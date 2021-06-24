package com.example.todo2.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.todo2.R
import com.example.todo2.databinding.ActivityEditBinding
import com.example.todo2.enums.Category
import com.example.todo2.enums.Priorities
import java.text.SimpleDateFormat
import java.time.LocalTime

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private var category = Category.HOME
    private var priority = Priorities.NORMAL
    private var pos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.acc.timePicker.setIs24HourView(true)
        binding.acc.create.text = "Edit task"
        val arrCat = ArrayAdapter.createFromResource(
                this,
                R.array.categories,
                R.layout.spinner_selected
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.acc.category.adapter = adapter
        }

        val arrPrio =  ArrayAdapter.createFromResource(
                this,
                R.array.priorities,
                R.layout.spinner_selected
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.acc.priority.adapter = adapter
        }

        binding.acc.category.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
                category = Category.valueOf(binding.acc.category.selectedItem.toString().toUpperCase())
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        })
        binding.acc.priority.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
                priority = Priorities.valueOf(binding.acc.priority.selectedItem.toString().toUpperCase())
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        })

        binding.acc.taskName.setText(intent.getStringExtra("title"))
        binding.acc.taskDes.setText(intent.getStringExtra("des"))
        binding.acc.datePicker.date = SimpleDateFormat("yyyy-MM-dd").parse(intent.getStringExtra("date")).getTime()
        val time = intent.getStringExtra("time")
        binding.acc.timePicker.hour = LocalTime.parse(time).hour
        binding.acc.timePicker.minute = LocalTime.parse(time).minute
        binding.acc.category.setSelection(arrCat.getPosition(intent.getStringExtra("category")?.toLowerCase()?.capitalize()))
        binding.acc.priority.setSelection(arrPrio.getPosition(intent.getStringExtra("priority")?.toLowerCase()?.capitalize()))
        pos = intent.getIntExtra("pos", -1)
    }

    fun addTask(view: View) {
        // send task data to main activity, validate input
        if (binding.acc.taskName.length() == 0 || binding.acc.taskName.equals("")) {
            binding.acc.taskName.setError("Task must have a name")
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val intent = Intent()
            intent.putExtra("name", binding.acc.taskName.text.toString())
            intent.putExtra("des", binding.acc.taskDes.text.toString())
            intent.putExtra("date", sdf.format(binding.acc.datePicker.date).toString())
            var timeString = binding.acc.timePicker.minute.toString()
            var timeHourString = binding.acc.timePicker.hour.toString()
            if (timeString.length == 1) {
                timeString = "0$timeString"
            }
            if (timeHourString.length == 1) {
                timeHourString = "0$timeHourString"
            }
            intent.putExtra("time", timeHourString + ":" + timeString)
            intent.putExtra("time", binding.acc.timePicker.hour.toString() + ":" + timeString)
            intent.putExtra("category", category.toString())
            intent.putExtra("priority", priority.toString())
            intent.putExtra("pos", pos)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}