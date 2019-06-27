package com.mutlucelep.todoapp.tasks

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mutlucelep.todoapp.MainActivity
import com.mutlucelep.todoapp.R
import com.mutlucelep.todoapp.utils.replaceFragmentInActivity
import com.mutlucelep.todoapp.utils.setupActionBar

class TaskActivity : AppCompatActivity() {
    private lateinit var dl_task: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        setupActionBar(R.id.tb_task) {
            setHomeAsUpIndicator(R.drawable.ic_menu)
            setDisplayHomeAsUpEnabled(true)
        }
        dl_task = (findViewById<DrawerLayout>(R.id.dl_task)).apply {
            setStatusBarBackground(R.color.colorPrimaryDark)
        }
        setupDrawerContent(findViewById(R.id.nv_task))

        val taskFragment = supportFragmentManager.findFragmentById(R.id.fl_task)
                as TaskFragment? ?: TaskFragment.newInstance().also {
            replaceFragmentInActivity(it, R.id.fl_task)
        }


    }

    private fun setupDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.statistics_navigation_menu_item) {
                val intent = Intent(this@TaskActivity, MainActivity::class.java)
                startActivity(intent)
            }

            menuItem.isChecked = true
            dl_task.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            dl_task.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}
