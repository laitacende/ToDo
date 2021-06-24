package com.example.todo2.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.todo2.enums.Category
import com.example.todo2.enums.Priorities
import com.example.todo2.R
import com.example.todo2.databinding.ActivityAddBinding

import java.text.SimpleDateFormat


class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var category = Category.HOME
    private var priority = Priorities.NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        val view = binding.root
        binding.timePicker.setIs24HourView(true)
        setContentView(view)
        ArrayAdapter.createFromResource(
            this,
                R.array.categories,
                R.layout.spinner_selected
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.category.adapter = adapter
        }
        ArrayAdapter.createFromResource(
                this,
                R.array.priorities,
                R.layout.spinner_selected
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.spinner_item)
            // Apply the adapter to the spinner
            binding.priority.adapter = adapter
        }
        binding.category.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
                category = Category.valueOf(binding.category.selectedItem.toString().toUpperCase())
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        })
        binding.priority.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View?, position: Int, id: Long) {
                priority = Priorities.valueOf(binding.priority.selectedItem.toString().toUpperCase())
            }
            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        })
    }

    fun addTask(view: View) {
        // send task data to main activity, validate input
        if (binding.taskName.length() == 0 || binding.taskName.equals("")) {
            binding.taskName.setError("Task must have a name")
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val intent = Intent()
            intent.putExtra("name", binding.taskName.text.toString())
            intent.putExtra("des", binding.taskDes.text.toString())
            intent.putExtra("date", sdf.format(binding.datePicker.date).toString())
            var timeString = binding.timePicker.minute.toString()
            var timeHourString = binding.timePicker.hour.toString()
            if (timeString.length == 1) {
                timeString = "0$timeString"
            }
            if (timeHourString.length == 1) {
                timeHourString = "0$timeHourString"
            }
            intent.putExtra("time", timeHourString + ":" + timeString)
            intent.putExtra("category", category.toString())
            intent.putExtra("priority", priority.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}