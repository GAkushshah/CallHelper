package com.greenapex.callhelper.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*

import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.CommonUtils
import com.greenapex.callhelper.Util.CommonUtils.context
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import kotlinx.android.synthetic.main.activity_note_create.*
import kotlinx.android.synthetic.main.activity_reminder_create.*

class NoteCreate : AppCompatActivity() {


    internal lateinit var dbHandler: MyDBHandler
    internal lateinit var bean: contactNote
    internal var contactNumber: String? = null
    internal var contactName: String? = null
    internal var contactNote: String? = null
    internal var contactImage: String? = null
    internal var edtContactName: String? = null
    internal var edtContactNote: String? = null
    internal var edt: String? = null
    internal var edtContactNumber: String? = null
    internal var edtContactId: Int = 0
    internal var len = 0
    internal var popup: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_create)

        edtWriteNote.imeOptions = EditorInfo.IME_ACTION_DONE
        edtWriteNote.setRawInputType(InputType.TYPE_CLASS_TEXT)

        dbHandler = MyDBHandler(this@NoteCreate)
        contactNote = edtWriteNote.text.toString()

        txtSelectContact.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                Pref.setValue(this@NoteCreate, "contactNumber", "")
                Pref.setValue(this@NoteCreate, "contactName", "")
                Pref.setValue(this@NoteCreate, "contactImage", "")

                startActivity(Intent(applicationContext, ContactList::class.java))
            }
        })

        btnSave.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                if (txtSelectContact.text.toString() == "") {
                    //customeToast("Please Enter Number")
                    Toast.makeText(this@NoteCreate, "Please Enter Number", Toast.LENGTH_SHORT).show()
                } else if (edtWriteNote.text.toString() == "") {
                    //customeToast("Please Enter Note")
                    Toast.makeText(this@NoteCreate, "Please Enter Note", Toast.LENGTH_SHORT).show()
                } else if (edt != null && edt!!.equals("editNote", ignoreCase = true)) {
                    noteUpdate()
                    edtWriteNote.setText("")
                    txtSelectContact.text = ""
                    finish()
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                } else {
                    callWebService()
                    edtWriteNote.setText("")
                    txtSelectContact.text = ""
                    finish()
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
            }
        })
        btnClose.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                edtWriteNote.setText("")
                txtSelectContact.text = ""
                finish()
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
        })

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                //text Erase then effect
                len = len - count
                txtNoteLimit.text = len.toString() + "/200"
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //text Add then effect
                len = count + len
                if (len == 200) {
                    Toast.makeText(this@NoteCreate, "full", Toast.LENGTH_SHORT).show()
                }
                txtNoteLimit.text = len.toString() + "/200"
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
        edtWriteNote.addTextChangedListener(textWatcher)
    }

    override fun onStart() {
        super.onStart()
        val intent = intent

        contactNumber = Pref.getValue(this@NoteCreate, "contactNumber1", null)
        contactName = Pref.getValue(this@NoteCreate, "contactName1", null)
        contactImage = Pref.getValue(this@NoteCreate, "contactImage1", null)

        if (contactNumber != null)
            if (contactNumber!!.length > 0) {
                txtSelectContact.text = "$contactName($contactNumber)"
                btnSelectContact.visibility = View.GONE
                txtSelectContact.isClickable = false
            } else {
                txtSelectContact.text = ""
            }

        dataEdit()
    }

    fun callWebService() {
        contactNote = edtWriteNote.text.toString()
        contactNumber = contactNumber!!.replace(" ", "")
        contactNumber = contactNumber!!.replace("+91", "")


        bean = contactNote()
        bean.contactNumber = contactNumber
        bean.contactName = contactName
        bean.contactNote = contactNote
        bean.contactImagePath = contactImage
        bean.type = "note"
        bean.contactNoteDateTime = CommonUtils.getDateTime()
        Log.d("CallHelper", "NoteCreate==> " + "Number" + bean.contactNumber
                + "Name" + bean.contactName
                + "Note" + bean.contactNote
                + "DATETIME" + bean.contactNoteDateTime
                + "ImagePath" + bean.contactImagePath)
        dbHandler.addNote(bean)
    }

    fun noteUpdate() {
        contactNote = edtWriteNote.text.toString()
        dbHandler.updateNote(contactNote!!, CommonUtils.getDateTime(), edtContactId)
    }

    fun dataEdit() {
        val intent = intent

        edtContactId = intent.getIntExtra("contactId", 0)
        edt = intent.getStringExtra("note")
        txtNoteLimit.text = len.toString() + "/200"
        popup = intent.getStringExtra("popup")


        if (edt != null && edt!!.equals("editNote", ignoreCase = true)) {
            bean = dbHandler.singleNote(edtContactId)
            edtContactNote = bean.contactNote
            edtContactNumber = bean.contactNumber
            edtContactName = bean.contactName
            btnSelectContact.visibility = View.GONE
            txtSelectContact.isClickable = false

            edtWriteNote.setText(edtContactNote)
            txtSelectContact.text = "$edtContactName($edtContactNumber)"
            len = edtWriteNote.text.length
            txtNoteLimit.text = len.toString() + "/200"
        }
    }
}
