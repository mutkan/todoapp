package com.mutlucelep.todoapp.tasks


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.mutlucelep.todoapp.R
import com.mutlucelep.todoapp.data.Task



/**
 * A simple [Fragment] subclass.
 * Use the [TaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class TaskFragment : Fragment(), TaskContractor.View {

    override lateinit var presenter: TaskContractor.Presenter

    override var isActive: Boolean = false
        get() = isAdded

    private lateinit var llTaskList: LinearLayout
    private lateinit var llNoTask: LinearLayout
    private lateinit var txtvTaskTitle: TextView
    private lateinit var imgvTaskIcon: ImageView
    private lateinit var tvTaskNoTaskInfo: TextView

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_task, container, false)

        with(root) {
            findViewById<RecyclerView>(R.id.rvTask).apply {

            }
            findViewById<SwipeRefreshLayout>(R.id.srlTask).apply {
                setOnRefreshListener {
                    presenter.loadTasks(false)
                }
            }

            llTaskList = findViewById(R.id.llTaskList)
            llNoTask = findViewById(R.id.llNoTask)
            txtvTaskTitle = findViewById(R.id.txtvTaskTitle)
            imgvTaskIcon = findViewById(R.id.imgvTaskIcon)
            tvTaskNoTaskInfo = findViewById(R.id.tvTaskNoTaskInfo)
        }

        requireActivity().findViewById<FloatingActionButton>(R.id.fab_task).apply {
            setImageResource(R.drawable.ic_add)
            setOnClickListener {
                presenter.addNewTask()
            }
        }

        setHasOptionsMenu(true)

        return root
    }


    override fun showTasks(tasks: List<Task>) {


    }

    override fun showAddTask() {

    }


    override fun showTaskMarkedActive() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskMarkedCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTaskDetailUi(taskId: String) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates
    }
    override fun showNoTasks() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoActiveTasks() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoCompletedTasks() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingIndicator(active: Boolean) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoadingTasksError() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showActiveFilterLabel() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCompletedFilterLabel() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAllFilterLabel() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        @JvmStatic
        fun newInstance() = TaskFragment()
    }
}
