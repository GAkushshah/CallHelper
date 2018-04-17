package com.greenapex.callhelper.Adpter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.telecom.Call
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.greenapex.callhelper.Activity.NoteCreate
import com.greenapex.callhelper.Fragment.CallNote
import com.greenapex.callhelper.MainActivity
import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import com.squareup.picasso.Picasso

/**
 * Created by GreenApex on 30/11/17.
 */

class adpterNote(internal var callNote: CallNote, internal var context: Context, internal var arrayNote: MutableList<contactNote>) : RecyclerView.Adapter<adpterNote.RecyclerViewHolder>() {
    //internal var inflater: LayoutInflater
    internal lateinit var dbHandler: MyDBHandler
    internal var Id: Int = 0
    internal var contactName: String? = null
    internal var contactNote: String? = null
    internal var contactNumber: String? = null
    internal var type: String? = null


    init {
      //  inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_contact_note, null, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val image: String?
        dbHandler = MyDBHandler(context)
        Id = arrayNote[position].contactId
        contactName = arrayNote[position].contactName
        contactNote = arrayNote[position].contactNote
        contactNumber = arrayNote[position].contactNumber
        type = arrayNote[position].type
        image = arrayNote[position].contactImagePath
        if (image != null && !image.isEmpty() && image != "") {
            val uri = Uri.parse(image)
            Picasso.with(context).load(uri).into(holder.imgPhoto)
            Log.d("call", "" + uri)
        } else {
            holder.imgPhoto.setImageResource(R.drawable.user)
        }

        if (contactName.equals("unknown", ignoreCase = true)) {
            holder.txtContactName.text = contactNumber
        } else {
            holder.txtContactName.text = contactName
        }
        holder.txtContactNote.text = contactNote
        CommonUtils.makeTextViewResizable(context, holder.txtContactNote, 1, "Read More", true)
        holder.txtContactDateTime.text = "created: " + arrayNote[position].contactNoteDateTime


        holder.btnSetting.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val note = arrayNote[position]

                val dialogBuilder = AlertDialog.Builder(context)
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.app_edit, null)
                dialogBuilder.setView(dialogView)
                val editDelete = dialogBuilder.create()
                editDelete.window!!.attributes.windowAnimations = R.style.DialogTheme
                editDelete.show()

                val relativeLayoutEdit = dialogView.findViewById<View>(R.id.relative_top) as RelativeLayout
                val relativeLayoutDelete = dialogView.findViewById<View>(R.id.relative_bottom) as RelativeLayout

                relativeLayoutEdit.setOnClickListener {
                    editDelete.cancel()
                    //clear sharedprefrece
                    //Pref.deleteAll(context)
//                    Pref.setValue(context,"contactNumber1","")
//                    Pref.setValue(context,"contactName1","")
//                    Pref.setValue(context,"contactImage1","")
                    val intent = Intent(context, NoteCreate::class.java)
                    intent.putExtra("note", "editNote")
                    intent.putExtra("contactId", note.contactId)
                    context.startActivity(intent)
                }
                relativeLayoutDelete.setOnClickListener {
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
                            dbHandler.deleteNote(note.contactId)
                            arrayNote.removeAt(position)
                            notifyItemRemoved(position)
                            notifyDataSetChanged()
                            delete.cancel()
                            val `val` = arrayNote.size
                            if (`val` == 0) {
                                callNote.visible()
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
            }
        })
    }

    override fun getItemCount(): Int {
        return arrayNote.size
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtContactName: TextView
        internal var txtContactNote: TextView
        internal var txtContactDateTime: TextView
        internal var btnSetting: Button
        internal var imgPhoto: ImageView

        init {
            btnSetting = itemView.findViewById<View>(R.id.btnNoteSetting) as Button
            txtContactName = itemView.findViewById<View>(R.id.txtNoteName) as TextView
            txtContactNote = itemView.findViewById<View>(R.id.txtNote) as TextView
            txtContactDateTime = itemView.findViewById<View>(R.id.txtNoteDateTime) as TextView
            imgPhoto = itemView.findViewById<View>(R.id.img_contact) as ImageView
        }
    }


}