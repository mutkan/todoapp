package com.mutlucelep.todoapp.data.source.local

import androidx.room.*
import com.mutlucelep.todoapp.data.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getTasks(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(task: Task)

    @Query("SELECT * FROM tasks WHERE id=:taskId")
    fun getTaskById(taskId: String): Task?

    @Update
    fun updateTask(updatedTask: Task)

    @Query("UPDATE tasks SET completed=:b WHERE id=:id")
    fun updateCompletedTask(id: String, b: Boolean)

    @Query("DELETE FROM tasks WHERE id=:taskId")
    fun deleteTaskById(taskId: String): Int

    @Query("DELETE FROM tasks")
    fun deleteTasks()

    @Query("DELETE FROM tasks WHERE completed=1")
    fun deleteCompletedTasks()
}