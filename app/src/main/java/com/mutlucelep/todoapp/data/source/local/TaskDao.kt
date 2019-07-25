package com.mutlucelep.todoapp.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mutlucelep.todoapp.data.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id=:taskId")
    fun getTaskById(taskId: String): Task?
}