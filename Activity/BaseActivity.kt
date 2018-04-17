package com.greenapex.callhelper.Activity

import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View

import com.greenapex.callhelper.R

/**
 * Created by GreenApex on 28/2/18.
 */

open class BaseActivity : AppCompatActivity() {

    internal lateinit var toolbar: Toolbar

    fun setupToolbar(title: String) {
        toolbar = findViewById<View>(R.id.toolbarSetting) as Toolbar

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }

        val actionBar = supportActionBar

        if (actionBar != null) {
            actionBar.title = title
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeButtonEnabled(true)
        }
    }



}
