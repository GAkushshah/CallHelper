package com.greenapex.callhelper.Fragment


import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.greenapex.callhelper.Activity.NoteCreate
import com.greenapex.callhelper.Adpter.adpterNote
import com.greenapex.callhelper.Adpter.adpterNoteReminder
import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import kotlinx.android.synthetic.main.fragment_call_note.*

import org.w3c.dom.Text


/**
 * A simple [Fragment] subclass.
 */
class CallNote : Fragment() {

    internal lateinit var fabNote: FloatingActionButton
//    internal var recyclerNote: RecyclerView
//    internal var txtNoteNull: TextView
//    internal var frameCallNote: FrameLayout
    internal lateinit var dbHandler: MyDBHandler
    internal lateinit var adpter: adpterNote
    internal var adpter1: adpterNoteReminder? = null
    internal lateinit var relativeCallNoteAdd: RelativeLayout
//    internal var relativeMain: RelativeLayout
    //internal lateinit var view: View
    internal  var list: List<contactNote>?=null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val  view = inflater!!.inflate(R.layout.fragment_call_note, container, false)
        fabNote = view.findViewById<View>(R.id.fabNote) as FloatingActionButton
//        recyclerNote = view.findViewById<View>(R.id.recyclerNote) as RecyclerView
//        txtNoteNull = view.findViewById<View>(R.id.txtNoteNull) as TextView
//        frameCallNote = view.findViewById<View>(R.id.frameCallNote) as FrameLayout
        relativeCallNoteAdd = view.findViewById<View>(R.id.relativeCallNoteAdd) as RelativeLayout
//        relativeMain = view.findViewById<View>(R.id.RelativeMain) as RelativeLayout
        dbHandler = MyDBHandler(context)

        fabNote.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {

//                Pref.deleteAll(context)
                Pref.setValue(context,"contactNumber1","")
                Pref.setValue(context,"contactName1","")
                Pref.setValue(context,"contactImage1","")


                startActivity(Intent(activity, NoteCreate::class.java))
                activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        })
        asynNote()
        return view
    }

    fun asynNote() {
        MyAsyncNote(activity).execute("")

    }

    internal inner class MyAsyncNote(var mContex: Activity) : AsyncTask<String, String, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
            CommonUtils.showProgress(context)
        }

        override fun doInBackground(vararg strings: String): String? {
            Note()
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)
            if (list!!.isEmpty()) {
                visible()
            } else {
                relativeCallNoteAdd.visibility = View.GONE
                txtNoteNull.visibility = View.GONE
                val layoutManager = LinearLayoutManager(context)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recyclerNote.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adpter = adpterNote(this@CallNote, context, list as MutableList<contactNote>)
                recyclerNote.adapter = adpter
            }
            CommonUtils.dismissProgress()
        }
    }

    fun Note() {
        list = dbHandler.selectAll("note")
    }

    override fun onResume() {
        super.onResume()
        asynNote()
    }

    fun visible() {
        relativeCallNoteAdd.visibility = View.VISIBLE
        txtNoteNull.visibility = View.VISIBLE
    }

    companion object {

        fun newInstance(): CallNote {
            return CallNote()
        }
    }
}// Required empty public constructor
