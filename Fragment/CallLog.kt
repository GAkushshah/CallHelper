package com.greenapex.callhelper.Fragment


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.greenapex.callhelper.Adpter.AdpterLog
import com.greenapex.callhelper.Model.conctactPojo
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import kotlinx.android.synthetic.main.fragment_call_log.*

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date


/**
 * A simple [Fragment] subclass.
 */
class CallLog : Fragment() {

    internal lateinit var mAdapter: RecyclerView.Adapter<*>
    internal var managedCursor: Cursor? = null
    internal lateinit var callLogItem: conctactPojo
    internal var mItems: ArrayList<conctactPojo>? = null
    internal lateinit var log_recycler: RecyclerView


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_call_log, container, false)
       log_recycler = view.findViewById<View>(R.id.log_recycler) as RecyclerView

        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                    Manifest.permission.READ_CALL_LOG)) {
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.READ_CALL_LOG), 1)
            } else {
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.READ_CALL_LOG), 1)
            }
        } else {
            asynCallLog()
        }

        return view
    }

    fun asynCallLog() {
        MyAsyncCallLog(activity).execute("")
    }

    internal inner class MyAsyncCallLog(var mContex: Activity) : AsyncTask<String, String, String>() {


        override fun onPreExecute() {
            super.onPreExecute()
            CommonUtils.dismissProgress()
        }

        override fun doInBackground(vararg strings: String): String? {
            getCallDetails()
            return null
        }

        override fun onPostExecute(s: String?) {
            super.onPostExecute(s)


            log_recycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mAdapter = AdpterLog(context, mItems)
            log_recycler.adapter = mAdapter
            CommonUtils.dismissProgress()
        }
    }

    private fun getCallDetails() {
        mItems = ArrayList()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        managedCursor = context.contentResolver.query(android.provider.CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC ")
        val number = managedCursor!!.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
        val type = managedCursor!!.getColumnIndex(android.provider.CallLog.Calls.TYPE)
        val date = managedCursor!!.getColumnIndex(android.provider.CallLog.Calls.DATE)
        val duration = managedCursor!!.getColumnIndex(android.provider.CallLog.Calls.DURATION)
        val name = managedCursor!!.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME)
        val image = managedCursor!!.getColumnIndex(android.provider.CallLog.Calls.CACHED_PHOTO_URI)

        while (managedCursor!!.moveToNext()) {
            val callName = managedCursor!!.getString(name)
            val phNumber = managedCursor!!.getString(number)
            val callType = managedCursor!!.getString(type)
            val callDate = managedCursor!!.getString(date)
            val photoUri = managedCursor!!.getString(image)
            val callDayTime = Date(java.lang.Long.valueOf(callDate)!!)

            val formatter = SimpleDateFormat("dd-MM-yy")
            val dateString = formatter.format(callDayTime)
            val callDuration = managedCursor!!.getString(duration)
            var dir: String? = null
            val dircode = Integer.parseInt(callType)
            when (dircode) {
                android.provider.CallLog.Calls.OUTGOING_TYPE -> dir = "OUTGOING"
                android.provider.CallLog.Calls.INCOMING_TYPE -> dir = "INCOMING"
                android.provider.CallLog.Calls.MISSED_TYPE -> dir = "MISSED"
                android.provider.CallLog.Calls.REJECTED_TYPE -> dir = "REJECTED"
            }
            callLogItem = conctactPojo()
            callLogItem.contactName = callName
            callLogItem.contactNumber = phNumber
            callLogItem.callDate = dateString
            callLogItem.callPhotoUri = photoUri
            // callLogItem.setCallDuration(callDuration);
            callLogItem.callType = dir
            mItems!!.add(callLogItem)
        }
        managedCursor!!.close()
        return
    }

    companion object {


        fun newInstance(): CallLog {
            return CallLog()
        }
    }
}// Required empty public constructor


