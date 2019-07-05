package com.mutlucelep.todoapp.data.source.local

import androidx.room.Dao
import androidx.room.Query
import com.mutlucelep.todoapp.data.Task

@Dao
interface TaskDao{
    @Query("SELECT * FROM tasks") fun getTasks(): List<Task>
}