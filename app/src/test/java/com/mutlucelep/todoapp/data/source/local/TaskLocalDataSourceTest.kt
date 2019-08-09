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