package com.example.todo2

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todo2.enums.Category
import com.example.todo2.enums.Priorities
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalTime
@Entity(tableName = "task")
@Parcelize
data class Task (
        @ColumnInfo(name = "title") var title: String,
        @ColumnInfo(name = "description") var description: String,
        @ColumnInfo(name = "date") var date: LocalDate,
        @ColumnInfo(name = "category") var category: Category,
        @ColumnInfo(name = "time") var time: LocalTime,
        @ColumnInfo(name="priority") var priority: Priorities,
        @ColumnInfo(name = "done") var done: Boolean) : Parcelable {

    @PrimaryKey(autoGenerate = true) var uid: Int = 0
}
