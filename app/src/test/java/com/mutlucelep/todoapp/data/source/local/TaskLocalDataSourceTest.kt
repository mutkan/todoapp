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

    @Test
    fun getTasks_retrievesSavedTasks() {
        with(taskLocalDataSource) {
            val task1 = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
            saveTask(task1)
            val task2 = Task(TASK_TITLE_2, TASK_DESCRIPTION_2)
            saveTask(task2)

            getTasks(object : TaskDataSource.LoadTasksCallback {
                override fun onTasksLoaded(tasks: List<Task>) {
                    assertNotNull(tasks)
                    assertThat(tasks.size, `is`(2))

                    var task1FoundId = false
                    var task2FoundId = false

                    for (task in tasks) {
                        if (task.id == task1.id) task1FoundId = true
                        if (task.id == task2.id) task2FoundId = true
                    }

                    assertTrue(task1FoundId)
                    assertTrue(task2FoundId)
                }

                override fun onDataNotAvailable() {
                    fail("Callback error")
                }
            })
        }
    }

    @Test
    fun completeTask_retrievesCompletedTask() {
        val newTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
        var callbackExecuted = false
        with(taskLocalDataSource) {
            saveTask(newTask)
            completeTask(newTask)

            getTask(newTask.id, object : TaskDataSource.GetTaskCallback {
                override fun onTaskLoaded(task: Task) {
                    callbackExecuted = true
                    assertNotNull(task)
                    assertThat(newTask, `is`(task))
                    assertThat(task.isCompleted, `is`(true))
                }

                override fun onDataNotAvailable() {
                    callbackExecuted = true
                    fail("Callback error")
                }
            })
        }

        assertTrue("Assertion were never actually run", callbackExecuted)
    }

    @Test
    fun activateTask_retrievesActivatedTask() {
        val newTask = Task(TASK_TITLE_1, TASK_DESCRIPTION_1)
        var callbackExecuted = false

        with(taskLocalDataSource) {
            saveTask(newTask)
            completeTask(newTask)
            activateTask(newTask)
            getTask(newTask.id, object : TaskDataSource.GetTaskCallback {
                override fun onTaskLoaded(task: Task) {
                    callbackExecuted = true
                    assertNotNull(task)
                    assertThat(task.isCompleted, `is`(false))
                }

                override fun onDataNotAvailable() {
                    callbackExecuted = true
                    fail("Callback error")
                }
            })
        }

        assertTrue("Assertions were never actually run", callbackExecuted)
    }

    @Test
    fun clearCompletedTask_retrievesTasks() {
        val newTask1 = Task(TASK_TITLE_1)
        val newTask2 = Task(TASK_TITLE_2)
        val newTask3 = Task(TASK_TITLE_3)
        var callbackExecuted = false
        with(taskLocalDataSource) {
            saveTask(newTask1)
            saveTask(newTask2)
            saveTask(newTask3)

            completeTask(newTask1)
            completeTask(newTask3)

            clearCompletedTasks()

            getTasks(object : TaskDataSource.LoadTasksCallback {
                override fun onTasksLoaded(tasks: List<Task>) {
                    callbackExecuted = true
                    assertThat(tasks.size, `is`(1))
                    assertThat(tasks[0].isCompleted, `is`(false))
                }

                override fun onDataNotAvailable() {
                    callbackExecuted = true
                    fail("Callback error")

                }
            })
        }

        assertTrue("Assertions were never actually run", callbackExecuted)
    }
    @Test
    fun deleteAllTasks_retrievesTasks(){
        val newTask1 = Task(TASK_TITLE_1)
        val newTask2 = Task(TASK_TITLE_2)

        val callback = mock(TaskDataSource.LoadTasksCallback::class.java)
        with(taskLocalDataSource){
            saveTask(newTask1)
            saveTask(newTask2)

            deleteAllTask()

            getTasks(callback)

            verify(callback).onDataNotAvailable()
            verify(callback, never()).onTasksLoaded(LinkedList<Task>())
        }
    }

    companion object {
        private val TASK_TITLE_1 = "TITLE1"
        private val TASK_TITLE_2 = "TITLE2"
        private val TASK_TITLE_3 = "TITLE3"
        private val TASK_DESCRIPTION_1 = "DESCRIPTION1"
        private val TASK_DESCRIPTION_2 = "DESCRIPTION2"
    }
}