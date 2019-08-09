package com.mutlucelep.todoapp.data.source.local

import androidx.room.Room
import com.mutlucelep.todoapp.data.Task
import com.mutlucelep.todoapp.data.source.TaskDataSource
import com.mutlucelep.todoapp.data.source.utils.SingleExecutors
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class TaskLocalDataSourceTest {
    private lateinit var taskLocalDataSource: TaskLocalDataSource
    private lateinit var database: ToDoDatabase


    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.systemContext, ToDoDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        TaskLocalDataSource.clearInstance()
        taskLocalDataSource = TaskLocalDataSource.getInstance(SingleExecutors(), database.taskDao())
    }

    @After
    fun endUp() {
        database.close()
        TaskLocalDataSource.clearInstance()
    }

    @Test
    fun testPreConditions() {
        assertNotNull(taskLocalDataSource)
    }

    @Test
    fun saveTask_retrievesTask() {
        // Given a new task
        val newTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)

        var callbackExecuted = false
        with(taskLocalDataSource) {
            // When saved into the persistent repository
            saveTask(newTask)

            // Then the task can be retrieved from the persistent repository
            getTask(newTask.id, object : TaskDataSource.GetTaskCallback {
                override fun onTaskLoaded(task: Task) {
                    assertThat(task, `is`(newTask))
                    callbackExecuted = true
                }

                override fun onDataNotAvailable() {
                    fail("Callback error")
                    callbackExecuted = true
                }
            })

        }

        assertTrue("Assertions were never actually run", callbackExecuted)
    }