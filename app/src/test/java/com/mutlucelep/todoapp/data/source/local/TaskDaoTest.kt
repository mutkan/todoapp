package com.mutlucelep.todoapp.data.source.local

import androidx.room.Room
import com.mutlucelep.todoapp.data.Task
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TaskDaoTest {
    private lateinit var database: ToDoDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, ToDoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTaskAndGetById() {
        database.taskDao().insertTask(DEFAULT_TASK)

        val insertedTask = database.taskDao().getTaskById(DEFAULT_TASK.id)
        assertTask(insertedTask, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_ID, DEFAULT_IS_COMPLETED)
    }

    @Test
    fun insertTaskReplacesOnConflict() {
        database.taskDao().insertTask(DEFAULT_TASK)

        val newTaskWithSameId = Task(NEW_TITLE, NEW_DESCRIPTION, DEFAULT_ID)

        database.taskDao().insertTask(newTaskWithSameId)

        val lastInsertedTask = database.taskDao().getTaskById(DEFAULT_ID)

        assertTask(lastInsertedTask, NEW_TITLE, NEW_DESCRIPTION, DEFAULT_ID, DEFAULT_IS_COMPLETED)
    }

    @Test
    fun insertTaskAndGetTasks() {
        database.taskDao().insertTask(DEFAULT_TASK)

        val tasks = database.taskDao().getTasks()

        assertThat(tasks.size, `is`(1))
        assertTask(tasks[0], DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_ID, DEFAULT_IS_COMPLETED)
    }

    @Test
    fun updateTaskAndGetById() {
        database.taskDao().insertTask(DEFAULT_TASK)

        val updatedTask = Task(NEW_TITLE, NEW_DESCRIPTION, DEFAULT_ID).apply {
            isCompleted = NEW_IS_COMPLETED
        }

        database.taskDao().updateTask(updatedTask)

        val loaded = database.taskDao().getTaskById(DEFAULT_ID)

        assertTask(loaded, NEW_TITLE, NEW_DESCRIPTION, DEFAULT_ID, NEW_IS_COMPLETED)
    }

    @Test
    fun updateCompletedAndGetById() {
        database.taskDao().insertTask(DEFAULT_TASK)

        database.taskDao().updateCompletedTask(DEFAULT_TASK.id, true)

        val updatedTask = database.taskDao().getTaskById(DEFAULT_TASK.id)

        assertTask(updatedTask, DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_ID, true)
    }

    @Test
    fun deleteTaskByIdAndGettingTasks() {
        database.taskDao().insertTask(DEFAULT_TASK)

        database.taskDao().deleteTaskById(DEFAULT_TASK.id)

        val tasks = database.taskDao().getTasks()

        assertThat(tasks.size, `is`(0))
    }

    @Test
    fun deleteTasksAndGettingTasks() {
        database.taskDao().insertTask(DEFAULT_TASK)

        database.taskDao().deleteTasks()

        val tasks = database.taskDao().getTasks()

        assertThat(tasks.size, `is`(0))
    }

    private fun assertTask(task: Task?, title: String, desc: String, id: String, isCompleted: Boolean) {
        assertNotNull(task as Task)
        assertThat(task.id, `is`(id))
        assertThat(task.title, `is`(title))
        assertThat(task.description, `is`(desc))
        assertThat(task.isCompleted, `is`(isCompleted))
    }


    companion object {
        private val DEFAULT_TITLE = "title"
        private val DEFAULT_DESCRIPTION = "description"
        private val DEFAULT_ID = "id"
        private val DEFAULT_IS_COMPLETED = false
        private val DEFAULT_TASK = Task(DEFAULT_TITLE, DEFAULT_DESCRIPTION, DEFAULT_ID).apply {
            isCompleted =
                DEFAULT_IS_COMPLETED
        }
        private val NEW_TITLE = "title2"
        private val NEW_DESCRIPTION = "description2"
        private val NEW_IS_COMPLETED = true
    }
}