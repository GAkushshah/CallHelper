package com.greenapex.callhelper.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.greenapex.callhelper.R

class TermsOfUseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_use)
        setupToolbar("Terms Of Use")
    }
}
