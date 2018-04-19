package com.greenapex.callhelper.Activity

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.greenapex.callhelper.Adpter.adpterNoteReminder
import com.greenapex.callhelper.Fragment.Reminder
import com.greenapex.callhelper.MainActivity
import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils.context
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import kotlinx.android.synthetic.main.activity_popup.*

class PopupActivity : Activity() {


    internal var isGoingIncoming: Boolean? = null
    internal var isGoingOutGoing: Boolean? = null
    internal var isGoingCallEnd: Boolean? = null
    internal var strName: String? = null
    internal var strNumber: String? = null
    internal var number: String? = null
    internal lateinit var dbHandler: MyDBHandler
    var adpter: adpterNoteReminder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)
//        recyclerView = findViewById<View>(R.id.recyclerPopup) as RecyclerView
//        txtNote = findViewById<View>(R.id.txtPopupaddNote) as TextView
//        txtReminder = findViewById<View>(R.id.txtPopupsetReminder) as TextView
//        txtNoData = findViewById<View>(R.id.txtNoData) as TextView
//        btnClose = findViewById<View>(R.id.btnPopupClose) as Button
//        linearPopup = findViewById<View>(R.id.LinearPopup) as LinearLayout
        dbHandler = MyDBHandler(this)
        number = Pref.getValue(this, "PhoneNumber", null)
        number = number!!.replace(" ", "")
        number = number!!.replace("+91", "")


        val list = dbHandler.noteReminder(number!!)
        isGoingIncoming = Pref.getValue(this, "isOnGoingIncoming", true)
        isGoingOutGoing = Pref.getValue(this, "isOnGoingOutgoing", true)
        isGoingCallEnd = Pref.getValue(this, "isOnGoingCallEnd", true)
        Log.d("Call_Popup", "" + isGoingCallEnd!!)
        if (list.size > 0) {
            strName = list[0].contactName
            strNumber = list[0].contactNumber
        } else {
            //            //thinking
            strName = ""
            strNumber = number
        }

        val extras = intent.extras
        if (extras != null) {
            val intent = intent
            val isOn = intent.getStringExtra("IsOn")
            if (isOn.equals("isOn", ignoreCase = true)) {
                LinearPopup.visibility = View.VISIBLE
            }
        }

        if (list.isEmpty()) {
            txtNoData.visibility = View.VISIBLE
            recyclerPopup.visibility = View.INVISIBLE
        }

        recyclerPopup.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adpter = adpterNoteReminder(this, list, "popup")
        recyclerPopup.adapter = adpter
        adpter!!.notifyDataSetChanged()

        btnPopupClose.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                finish()
            }
        })

        btnPopupLaunch.setOnClickListener(object :OnOneOffClickListener(){
            override fun onSingleClick(v: View?) {
                val intent = Intent(this@PopupActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
//                Toast.makeText(this@PopupActivity, "Hi there! This is a Toast.", Toast.LENGTH_SHORT).show()
            }
        })

        txtPopupsetReminder.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                //  Pref.deleteAll(this@PopupActivity)
                val intent = Intent(this@PopupActivity, ReminderCreate::class.java)
                intent.putExtra("popup", "popup")
                intent.putExtra("contactName", strName)
                intent.putExtra("contactNumber", strNumber)
                startActivity(intent)
                finish()
            }
        })

        txtPopupaddNote.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                // Pref.deleteAll(this@PopupActivity)
                val intent = Intent(this@PopupActivity, NoteCreate::class.java)
                Pref.setValue(this@PopupActivity, "contactNumber1", strNumber)
                Pref.setValue(this@PopupActivity, "contactName1", strName)
                startActivity(intent)
                finish()
            }
        })
    }
}
