package com.greenapex.callhelper.Adpter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.greenapex.callhelper.Activity.NoteCreate
import com.greenapex.callhelper.Activity.ReminderCreate
import com.greenapex.callhelper.Model.conctactPojo
import com.greenapex.callhelper.R
import com.greenapex.callhelper.R.id.btnSetting
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.app_toolbar.*
import outlander.showcaseview.ShowcaseViewBuilder

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

/**
 * Created by GreenApex on 9/11/17.
 */

class AdpterLog(internal var context: Context, internal var arraySchedule: ArrayList<conctactPojo>?) : RecyclerView.Adapter<AdpterLog.RecyclerViewHolder>() {
    //    internal var inflater: LayoutInflater
    internal var number: String? = null
    internal var name: String? = null
    internal lateinit var adpter: adpterNoteReminder
    internal lateinit var dbHandler: MyDBHandler
    internal var managedCursor: Cursor? = null
    internal lateinit var callLogItem: conctactPojo

    internal lateinit var txt_logInfoName: TextView
    internal lateinit var txt_logInfoNumber: TextView
    internal lateinit var txt_addNote: TextView
    internal lateinit var txt_setReminder: TextView
    internal lateinit var btn_call: Button
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var mAdapter: RecyclerView.Adapter<*>
    var showcaseViewBuilder: ShowcaseViewBuilder? = null



    init {
        //  inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_logrow, null, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        val image: String?
        viewHolder.txtlogNumber.text = arraySchedule?.get(position)!!.contactNumber
        number = arraySchedule?.get(position)!!.contactNumber
        number = number!!.replace(" ", "")

        viewHolder.txtCallDate.text = arraySchedule!![position].callDate
        image = arraySchedule!![position].callPhotoUri

        val callName = arraySchedule!![position].contactName
        if (callName == null) {
            viewHolder.txtlogName.text = "Unknown"
        } else {
            viewHolder.txtlogName.text = callName
        }

        val type = arraySchedule!![position].callType
        if (type != null && type.equals("OUTGOING", ignoreCase = true)) {
            viewHolder.imageView.setImageResource(R.drawable.outgoing)
            viewHolder.txtlogName.setTextColor(Color.BLACK)
            viewHolder.txtlogNumber.setTextColor(Color.BLACK)
        } else if (type != null && type.equals("INCOMING", ignoreCase = true)) {
            viewHolder.imageView.setImageResource(R.drawable.incoming)
            viewHolder.txtlogName.setTextColor(Color.BLACK)
            viewHolder.txtlogNumber.setTextColor(Color.BLACK)
        } else if (type != null && type.equals("MISSED", ignoreCase = true)) {
            viewHolder.imageView.setImageResource(R.drawable.misscall)
            viewHolder.txtlogName.setTextColor(Color.RED)
            viewHolder.txtlogNumber.setTextColor(Color.RED)
        } else if (type != null && type.equals("REJECTED", ignoreCase = true)) {
            viewHolder.imageView.setImageResource(R.drawable.notreceive)
            viewHolder.txtlogName.setTextColor(Color.GRAY)
            viewHolder.txtlogNumber.setTextColor(Color.GRAY)
        }


        viewHolder.itemView.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {

                val txt_logInfoName: TextView
                val txt_logInfoNumber: TextView
                val txt_addNote: TextView
                val txt_setReminder: TextView
                val txtNoData1: TextView
                val btn_call: Button
                val imgContact: ImageView
                val recyclerView: RecyclerView

                dbHandler = MyDBHandler(context)
                val mItems = ArrayList<conctactPojo>()

                val dialogBuilder = AlertDialog.Builder(context)
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.app_loginfo, null)

                dialogBuilder.setView(dialogView)

                val a = dialogBuilder.create()

                txt_logInfoName = dialogView.findViewById<View>(R.id.txt_logInfoName) as TextView
                txt_logInfoNumber = dialogView.findViewById<View>(R.id.txt_logInfoNumber) as TextView
                txt_addNote = dialogView.findViewById<View>(R.id.txtaddNote) as TextView
                txt_setReminder = dialogView.findViewById<View>(R.id.txtsetReminder) as TextView
                btn_call = dialogView.findViewById<View>(R.id.btnCall) as Button
                imgContact = dialogView.findViewById<View>(R.id.img_logInfo) as ImageView
                txtNoData1 = dialogView.findViewById<View>(R.id.txtNoData1) as TextView
                txt_logInfoName.text = viewHolder.txtlogName.text
                txt_logInfoNumber.text = viewHolder.txtlogNumber.text
                if (image != null) {
                    val uri = Uri.parse(image)
                    Picasso.with(context).load(uri).into(imgContact)
                } else {
                    imgContact.setImageResource(R.drawable.user)
                }
                name = txt_logInfoName.text.toString()
                number = txt_logInfoNumber.text.toString()
                number = number!!.replace(" ", "")
                number = number!!.replace("+91", "")


                txt_addNote.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val intent = Intent(context, NoteCreate::class.java)
                        //   Pref.deleteAll(context)

//                        intent.putExtra("popup", "popup")
//                        intent.putExtra("contactName", name)
//                        intent.putExtra("contactNumber", number)
                        Pref.setValue(context, "contactNumber1", number)
                        Pref.setValue(context, "contactName1", name)
                        context.startActivity(intent)
                    }
                })
                txt_setReminder.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val intent = Intent(context, ReminderCreate::class.java)
//                        Pref.deleteAll(context)
                        intent.putExtra("popup", "popup")
                        intent.putExtra("contactName", name)
                        intent.putExtra("contactNumber", number)
                        context.startActivity(intent)
                    }
                })

                recyclerView = dialogView.findViewById<View>(R.id.log_contactInfo) as RecyclerView

                btn_call.setOnClickListener(object : OnOneOffClickListener() {
                    @SuppressLint("MissingPermission")
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number))
                        context.startActivity(i)
                    }
                })
                val list = dbHandler.noteReminder(number!!)

                if (list.isEmpty()) {
                    txtNoData1.visibility = View.VISIBLE
                } else {
                    txtNoData1.visibility = View.GONE
                    val layoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                    recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adpter = adpterNoteReminder(context, list)
                    recyclerView.adapter = adpter
                    adpter.notifyDataSetChanged()
                }


                val btnCancel = dialogView.findViewById<View>(R.id.btnInfoClose) as Button
                btnCancel.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                    }
                })
                a.window!!.attributes.windowAnimations = R.style.DialogTheme
                a.show()
            }

        })

        viewHolder.imgInfo.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {

                val dialogBuilder = AlertDialog.Builder(context)
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.app_log_info_click, null)

                dialogBuilder.setView(dialogView)

                val a = dialogBuilder.create()

                txt_logInfoName = dialogView.findViewById<View>(R.id.txt_logInfo_click_Name) as TextView
                txt_logInfoNumber = dialogView.findViewById<View>(R.id.txt_logInfo_click_Number) as TextView
                txt_addNote = dialogView.findViewById<View>(R.id.txtaddNote_click) as TextView
                txt_setReminder = dialogView.findViewById<View>(R.id.txtsetReminder_click) as TextView
                recyclerView = dialogView.findViewById<View>(R.id.log_contactInfo_click) as RecyclerView

                btn_call = dialogView.findViewById<View>(R.id.btnCall_click) as Button
                txt_logInfoName.text = viewHolder.txtlogName.text
                txt_logInfoNumber.text = viewHolder.txtlogNumber.text
                name = txt_logInfoName.text.toString()
                number = txt_logInfoNumber.text.toString()
                number = number!!.replace(" ", "")
                number = number!!.replace("+91", "")



                btn_call.setOnClickListener(object : OnOneOffClickListener() {
                    @SuppressLint("MissingPermission")
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val i = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number))
                        context.startActivity(i)
                    }
                })

                getNumberLog()

                txt_addNote.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val intent = Intent(context, NoteCreate::class.java)
                        //   Pref.deleteAll(context)
//                        intent.putExtra("popup", "popup")
//                        intent.putExtra("contactName", name)
//                        intent.putExtra("contactNumber", number)
                        Pref.setValue(context, "contactNumber1", number)
                        Pref.setValue(context, "contactName1", name)
                        context.startActivity(intent)

                    }
                })
                txt_setReminder.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val intent = Intent(context, ReminderCreate::class.java)
                        //   Pref.deleteAll(context)
                        intent.putExtra("popup", "popup")
                        intent.putExtra("contactName", name)
                        intent.putExtra("contactNumber", number)
                        context.startActivity(intent)
                    }
                })


                a.window!!.attributes.windowAnimations = R.style.DialogTheme
                val btnCancel = dialogView.findViewById<View>(R.id.btnInfo_click_Close) as Button
                btnCancel.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                    }
                })
                a.show()
            }
        })

    }

    override fun getItemCount(): Int {
        return arraySchedule!!.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtlogNumber: TextView
        internal var txtlogName: TextView
        internal var txtCallDate: TextView
        internal var imageView: ImageView
        internal var imgInfo: ImageView

        init {
            imgInfo = itemView.findViewById<View>(R.id.img_info) as ImageView
            imageView = itemView.findViewById<View>(R.id.img_callType) as ImageView
            txtlogNumber = itemView.findViewById<View>(R.id.txt_logNumber) as TextView
            txtlogName = itemView.findViewById<View>(R.id.txt_logName) as TextView
            txtCallDate = itemView.findViewById<View>(R.id.txt_logTime) as TextView
        }


    }

//    private fun showcaseSettingBtn() {
//        showcaseViewBuilder = ShowcaseViewBuilder.init(context as Activity?)
//        showcaseViewBuilder!!.setTargetView()
//                .setBackgroundOverlayColor(-0x11b2b2b3)
//                .setRingColor(Color.parseColor("#e0c907"))
//                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, context.getResources().getDisplayMetrics()))
//                .setMarkerDrawable(context.getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
//                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, context.getResources().getDisplayMetrics()))
//                .addCustomView(R.layout.setting_description_view, Gravity.BOTTOM)
//                .setCustomViewMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, context.getResources().getDisplayMetrics()).toInt())
//        showcaseViewBuilder!!.show()
//        showcaseViewBuilder!!.setClickListenerOnView(R.id.btn, object: View.OnClickListener {
//            override fun onClick(v:View) {
//                showcaseViewBuilder!!.hide()
//            }
//        })
//    }

    fun getNumberLog() {
        val mItems = ArrayList<conctactPojo>()

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


        //   number=modifyNumber(number);

        managedCursor = context.contentResolver.query(android.provider.CallLog.Calls.CONTENT_URI, null, "NUMBER='$number'", null, android.provider.CallLog.Calls.DATE + " DESC ")

        if (managedCursor != null) {

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

                val formatter = SimpleDateFormat("dd-MM-yy HH:mm:ss")
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
                callLogItem.callDuration = callDuration
                callLogItem.callType = dir
                mItems.add(callLogItem)

                if (mItems.isEmpty()) {
                    recyclerView.visibility = View.GONE
                }
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                mAdapter = adpterInfo(context, mItems)
                recyclerView.adapter = mAdapter
            }
        }
    }


}