package com.greenapex.callhelper.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.greenapex.callhelper.R

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setupToolbar("About Us")
    }
}
