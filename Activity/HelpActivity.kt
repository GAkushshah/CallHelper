package com.greenapex.callhelper.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.OnOneOffClickListener
import kotlinx.android.synthetic.main.activity_help.*
import android.content.Intent
import android.net.Uri
import android.net.Uri.fromParts
import android.widget.Toast


class HelpActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        setupToolbar("Help")
        btnSendMail.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View?) {

                if (edtHelp.text.toString() == "") {
                    Toast.makeText(this@HelpActivity, "Please Enter The Question", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "kushh5595@gmail.com", null))
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Help For Application")
                    intent.putExtra(Intent.EXTRA_TEXT, edtHelp.text.toString())
                    startActivity(Intent.createChooser(intent, "Choose an Email client :"))
                    edtHelp.setText("")
                }
            }

        })
    }
}
