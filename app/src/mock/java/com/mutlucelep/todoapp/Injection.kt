import android.content.Context
import com.mutlucelep.todoapp.data.FakeTaskRemoteDataSource
import com.mutlucelep.todoapp.data.source.TaskRepository
import com.mutlucelep.todoapp.data.source.local.TaskLocalDataSource
import com.mutlucelep.todoapp.data.source.local.ToDoDatabase
import com.mutlucelep.todoapp.utils.AppExecutors

object Injection{
    fun provideTaskRepository(context: Context): TaskRepository{
        val database = ToDoDatabase.getInstance(context)
        return TaskRepository.getInstance(FakeTaskRemoteDataSource.getInstance(), TaskLocalDataSource.getInstance(
            AppExecutors(), database.taskDao()
        ))
    }
}