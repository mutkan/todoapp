package com.mutlucelep.todoapp.data

import androidx.room.Entity


@Entity(tableName = "tasks")
data class Task(val title: String, val description: String, val id: String, val isCompleted: Boolean) {
    val isActive: Boolean
        get() = !isCompleted
}