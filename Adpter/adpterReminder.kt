package com.greenapex.callhelper.Adpter


import android.content.Context
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.greenapex.callhelper.Fragment.Reminder
import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.AlarmUtils
import com.greenapex.callhelper.Util.CommonUtils
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import com.squareup.picasso.Picasso

/**
 * Created by GreenApex on 5/12/17.
 */

class adpterReminder(internal var mReminder: Reminder, internal var context: Context, internal var arrayReminder: MutableList<contactNote>) : RecyclerView.Adapter<adpterReminder.RecyclerViewHolder>() {
 //   internal var inflater: LayoutInflater
    internal lateinit var dbHandler: MyDBHandler
    internal var Id: Int = 0
    internal var contactName: String?=null
    internal var cNote: String?=null
    internal var contactNumber: String?=null
    internal var type: String?=null

    init {
   //     inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_contact_reminder, null, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        dbHandler = MyDBHandler(context)
        val image: String?
        Id = arrayReminder[position].contactId
        contactName = arrayReminder[position].contactName
        cNote = arrayReminder[position].contactNote
        contactNumber = arrayReminder[position].contactNumber
        type = arrayReminder[position].type
        image = arrayReminder[position].contactImagePath
        if (image != null && !image.isEmpty() && image != "") {
            val uri = Uri.parse(image)
            Picasso.with(context).load(uri).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.user)
        }

        if (contactName.equals("unknown", ignoreCase = true) || contactName.equals("", ignoreCase = true)) {
            holder.txtReminderName.text = contactNumber
        } else {
            holder.txtReminderName.text = contactName
        }

        holder.txtReminderNote.text = cNote
        CommonUtils.makeTextViewResizable(context, holder.txtReminderNote, 1, "Read More", true)
        holder.txtReminderCreateDateTime.text = "created: " + arrayReminder[position].contactNoteDateTime
        holder.txtReminderDateTime.text = arrayReminder[position].date + " " + arrayReminder[position].time

        holder.btnReminderSetting.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {

                val reminder = arrayReminder[position]

                val dialogBuilder = AlertDialog.Builder(context)
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.app_reminder_edit, null)
                dialogBuilder.setView(dialogView)
                val editDelete = dialogBuilder.create()
                editDelete.window!!.attributes.windowAnimations = R.style.DialogTheme
                editDelete.show()
                val relativeLayoutDelete = dialogView.findViewById<View>(R.id.relative_bottom) as RelativeLayout


                relativeLayoutDelete.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        editDelete.cancel()
                        val dialogBuilder = AlertDialog.Builder(context)
                        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                        val dialogView = inflater.inflate(R.layout.app_delete, null)
                        dialogBuilder.setView(dialogView)
                        val delete = dialogBuilder.create()
                        delete.show()
                        val btnDelete = dialogView.findViewById<View>(R.id.btnDelete) as Button

                        btnDelete.setOnClickListener(object : OnOneOffClickListener() {
                            override fun onSingleClick(v: View) {
                                //Toast.makeText(context, "Delete" + reminder.contactId, Toast.LENGTH_SHORT).show()
                                AlarmUtils.cancelAlarm(context, reminder.contactId)
                                dbHandler.deleteNote(reminder.contactId)
                                arrayReminder.removeAt(position)
                                notifyItemRemoved(position)
                                notifyDataSetChanged()
                                delete.cancel()
                                val `val` = arrayReminder.size
                                if (`val` == 0) {
                                    mReminder.visible()
                                }
                            }
                        })

                        val btnClose = dialogView.findViewById<View>(R.id.btnClose) as Button
                        btnClose.setOnClickListener(object : OnOneOffClickListener() {
                            override fun onSingleClick(v: View) {
                                delete.cancel()
                            }
                        })
                    }
                })
            }
        })
    }

    override fun getItemCount(): Int {
        return arrayReminder.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtReminderName: TextView
        internal var txtReminderNote: TextView
        internal var txtReminderCreateDateTime: TextView
        internal var txtReminderDateTime: TextView
        internal var btnReminderSetting: Button
        internal var imageView: ImageView

        init {
            btnReminderSetting = itemView.findViewById<View>(R.id.btnReminderSetting) as Button
            txtReminderName = itemView.findViewById<View>(R.id.txtReminderName) as TextView
            txtReminderNote = itemView.findViewById<View>(R.id.txtReminderNote) as TextView
            txtReminderCreateDateTime = itemView.findViewById<View>(R.id.txtReminderCreateDateTime) as TextView
            txtReminderDateTime = itemView.findViewById<View>(R.id.txtReminderDateTime) as TextView
            imageView = itemView.findViewById<View>(R.id.imgReminderContact) as ImageView
        }
    }
}
