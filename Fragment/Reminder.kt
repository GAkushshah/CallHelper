package com.greenapex.callhelper.Fragment


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.greenapex.callhelper.Activity.ReminderCreate
import com.greenapex.callhelper.Adpter.adpterReminder
import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import kotlinx.android.synthetic.main.fragment_reminder.*

/**
 * A simple [Fragment] subclass.
 */
class Reminder : Fragment() {

    internal lateinit var fabReminder: FloatingActionButton
//    internal var recyclerReminder: RecyclerView
//    internal var txtReminderNull: TextView
    internal lateinit var relativeCallReminderAdd: RelativeLayout

    internal lateinit var dbHandler: MyDBHandler
    internal lateinit var adpter: adpterReminder
    internal lateinit var list: List<contactNote>


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_reminder, container, false)
        dbHandler = MyDBHandler(context)
//        txtReminderNull = view.findViewById<View>(R.id.txtReminderNull) as TextView
        fabReminder = view.findViewById<View>(R.id.fabReminder) as FloatingActionButton
//        recyclerReminder = view.findViewById<View>(R.id.recyclerReminder) as RecyclerView
        relativeCallReminderAdd = view.findViewById<View>(R.id.relativeCallReminderAdd) as RelativeLayout
        fabReminder.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {

//                Pref.deleteAll(context)
                Pref.setValue(context,"contactNumber1","")
                Pref.setValue(context,"contactName1","")
                Pref.setValue(context,"contactImage1","")

                startActivity(Intent(activity, ReminderCreate::class.java))
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        })
        asynReminder()
        return view
    }

    fun asynReminder() {
        MyAsyncReminder(activity).execute("")

    }

    internal inner class MyAsyncReminder(var mContex: Activity) : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
            CommonUtils.showProgress(context)
        }

        override fun doInBackground(vararg strings: String): String? {
            list = dbHandler.selectAll("reminder")

            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (list.isEmpty()) {
                visible()
            } else {
                relativeCallReminderAdd.visibility = View.GONE
                txtReminderNull.visibility = View.GONE
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recyclerReminder.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adpter = adpterReminder(this@Reminder, context, list as MutableList<contactNote>)
                recyclerReminder.adapter = adpter
            }
            CommonUtils.dismissProgress()
        }
    }


    override fun onResume() {
        super.onResume()
        asynReminder()
    }

    fun visible() {
        relativeCallReminderAdd.visibility = View.VISIBLE
        txtReminderNull.visibility = View.VISIBLE
    }

    companion object {

        fun newInstance(): Reminder {
            return Reminder()
        }
    }
}// Required empty public constructor
