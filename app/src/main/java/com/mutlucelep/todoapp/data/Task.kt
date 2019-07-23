package com.mutlucelep.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "tasks")
data class Task(val title: String, val description: String, @PrimaryKey val id: String = UUID.randomUUID().toString()) {

    var isCompleted : Boolean = false

    val isActive: Boolean
        get() = !isCompleted
}