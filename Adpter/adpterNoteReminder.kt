package com.greenapex.callhelper.Adpter

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import com.squareup.picasso.Picasso

/**
 * Created by GreenApex on 8/12/17.
 */

class adpterNoteReminder : RecyclerView.Adapter<adpterNoteReminder.RecyclerViewHolder> {

//    internal var inflater: LayoutInflater

    internal var context: Context
    internal var arrayReminder: List<contactNote>
    internal var Id: Int = 0
    internal var contactName: String?=null
    internal var cNote: String?=null
    internal var contactNumber: String?=null
    internal var type: String?=null
    internal var isfrom = ""

    constructor(context: Context, arrayReminder: List<contactNote>) {
        this.arrayReminder = arrayReminder
        this.context = context
  //      inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    constructor(context: Context, arrayReminder: List<contactNote>, isfrom: String) {
        this.arrayReminder = arrayReminder
        this.context = context
        this.isfrom = isfrom
      //  inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_contact_notreminder, null, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val image: String
        Id = arrayReminder[position].contactId
        contactName = arrayReminder[position].contactName
        cNote = arrayReminder[position].contactNote
        contactNumber = arrayReminder[position].contactNumber
        type = arrayReminder[position].type

        //        image = arrayReminder.get(position).getContactImagePath();
        //        if (image != null) {
        //            Uri uri = Uri.parse(image);
        //            Picasso.with(context).load(uri).into(holder.imageView);
        //            Log.d("call", "" + image);
        //        } else {
        //            holder.imageView.setImageResource(R.drawable.user);
        //        }
        //        holder.txtReminderName.setText(contactName);


        holder.txtReminderNote.text = cNote
        CommonUtils.makeTextViewResizable(context, holder.txtReminderNote, 1, "Read More", true)
        holder.txtReminderCreateDateTime.text = "created: " + arrayReminder[position].contactNoteDateTime
        if (arrayReminder[position].date == null && arrayReminder[position].time == null) {
            holder.img.visibility = View.GONE
            holder.txtReminderDateTime.visibility = View.GONE
        }
        holder.txtReminderDateTime.text = arrayReminder[position].date + " " + arrayReminder[position].time

    }

    override fun getItemCount(): Int {
        return if (isfrom.equals("popup", ignoreCase = true) && arrayReminder.size > 3) {
            3
        } else {
            arrayReminder.size
        }
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtReminderNote: TextView
        internal var txtReminderCreateDateTime: TextView
        internal var txtReminderDateTime: TextView
        internal var img: ImageView

        init {
            txtReminderNote = itemView.findViewById<View>(R.id.txtNoteReminderNote) as TextView
            txtReminderCreateDateTime = itemView.findViewById<View>(R.id.txtNoteReminderCreateDateTime) as TextView
            txtReminderDateTime = itemView.findViewById<View>(R.id.txtNoteReminderDateTime) as TextView
            img = itemView.findViewById<View>(R.id.NoteReminderimg) as ImageView
        }
    }
}
