package com.greenapex.callhelper.Adpter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SectionIndexer
import android.widget.TextView

import com.greenapex.callhelper.Activity.NoteCreate
import com.greenapex.callhelper.Activity.ReminderCreate
import com.greenapex.callhelper.Model.conctactPojo
import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import com.squareup.picasso.Picasso

import java.util.ArrayList

/**
 * Created by GreenApex on 24/11/17.
 */

class adpterContact(internal var context: Context, internal var arrayContact: List<conctactPojo>) : RecyclerView.Adapter<adpterContact.RecyclerViewHolder>(), SectionIndexer {
    // internal var inflater: LayoutInflater
    internal lateinit var dbHandler: MyDBHandler
    internal lateinit var adpter: adpterNoteReminder
    internal var number: String? = null
    internal var name: String? = null
    private var mSectionPositions: ArrayList<Int>? = null

    init {
        // inflater = context.getSystemService(context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_contactrow, null, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        val image: String?
        viewHolder.txtContactName.text = arrayContact[position].contactName
        viewHolder.txtContactNumber.text = arrayContact[position].contactNumber
        image = arrayContact[position].callPhotoUri
        if (image != null) {
            val uri = Uri.parse(image)
            Picasso.with(context).load(uri).into(viewHolder.imgContact)
        } else {
            viewHolder.imgContact.setImageResource(R.drawable.user)
        }
        number = arrayContact[position].contactNumber
        number = number!!.replace(" ", "")

        viewHolder.itemView.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val txt_logInfoName: TextView
                val txt_logInfoNumber: TextView
                val txt_addNote: TextView
                val txt_setReminder: TextView
                val txtNoData1: TextView
                val btn_call: Button
                val imgPhoto: ImageView
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
                txtNoData1 = dialogView.findViewById<View>(R.id.txtNoData1) as TextView
                btn_call = dialogView.findViewById<View>(R.id.btnCall) as Button
                imgPhoto = dialogView.findViewById<View>(R.id.img_logInfo) as ImageView
                txt_logInfoName.text = viewHolder.txtContactName.text
                txt_logInfoNumber.text = viewHolder.txtContactNumber.text

                if (image != null) {
                    val uri = Uri.parse(image)
                    Picasso.with(context).load(uri).into(imgPhoto)
                } else {
                    viewHolder.imgContact.setImageResource(R.drawable.user)
                }
                name = txt_logInfoName.text.toString()
                number = txt_logInfoNumber.text.toString()
                number = number!!.replace(" ", "")
                number = number!!.replace("+91", "")


                txt_addNote.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val intent = Intent(context, NoteCreate::class.java)
                        //  Pref.deleteAll(context)
                        Pref.setValue(context, "contactNumber1", number)
                        Pref.setValue(context, "contactName1", name)
//                        intent.putExtra("popup", "popup")
//                        intent.putExtra("contactName", name)
//                        intent.putExtra("contactNumber", number)
                        context.startActivity(intent)
                    }
                })
                txt_setReminder.setOnClickListener(object : OnOneOffClickListener() {
                    override fun onSingleClick(v: View) {
                        a.cancel()
                        val intent = Intent(context, ReminderCreate::class.java)
                        // Pref.deleteAll(context)
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

    }

    override fun getItemCount(): Int {
        return arrayContact.size
    }

    override fun getSections(): Array<String> {
        val sections = ArrayList<String>(26)
        mSectionPositions = ArrayList(26)
        var i = 0
        val size = arrayContact.size
        while (i < size) {
            val section = arrayContact[i].contactName.toString().toUpperCase()
            val word = section.substring(0, 1)
            if (!sections.contains(word)) {
                sections.add(word)
                mSectionPositions!!.add(i)
            }
            i++
        }
        return sections.toTypedArray<String>()

    }

    override fun getPositionForSection(sectionIndex: Int): Int {
        return mSectionPositions!![sectionIndex]

    }

    override fun getSectionForPosition(position: Int): Int {
        return 0
    }

    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var txtContactName: TextView
        internal var txtContactNumber: TextView
        internal var imgContact: ImageView

        init {
            txtContactName = itemView.findViewById<View>(R.id.txt_contactName) as TextView
            txtContactNumber = itemView.findViewById<View>(R.id.txt_contactNumber) as TextView
            imgContact = itemView.findViewById<View>(R.id.img_contact) as ImageView
        }
    }
}
