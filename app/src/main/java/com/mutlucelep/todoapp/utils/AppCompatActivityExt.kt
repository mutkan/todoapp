package com.mutlucelep.todoapp.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mutlucelep.todoapp.R


fun AppCompatActivity.setupActionBar(@IdRes id: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(id))
    supportActionBar?.run {
        action()
    }
}

inline fun FragmentManager.transact(func: FragmentTransaction.()->FragmentTransaction){
    beginTransaction().func().commit()

}

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes id: Int){
    supportFragmentManager.transact { replace(id,fragment) }

}
