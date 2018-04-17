package com.greenapex.callhelper.Activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.greenapex.callhelper.MainActivity

import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import kotlinx.android.synthetic.main.activity_showcase.*

class ShowcaseActivity : AppCompatActivity() {

    internal var showcase: String? = "showCase"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showcase)

        btnShowcaseSkip.setOnClickListener(object : OnOneOffClickListener(){

            override fun onSingleClick(v: View?) {
                val i = Intent(this@ShowcaseActivity, MainActivity::class.java)
                startActivity(i)

                // close this activity
                finish()
            }

        })
    }
}
