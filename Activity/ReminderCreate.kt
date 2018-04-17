package com.greenapex.callhelper.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast

import com.greenapex.callhelper.Model.contactNote
import com.greenapex.callhelper.R

import com.greenapex.callhelper.Util.AlarmUtils
import com.greenapex.callhelper.Util.CommonUtils
import com.greenapex.callhelper.Util.CommonUtils.context
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import com.greenapex.callhelper.dbCallHelper.MyDBHandler
import kotlinx.android.synthetic.main.activity_reminder_create.*

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Created by GreenApex on 5/12/17.
 */

class ReminderCreate : AppCompatActivity() {


    internal var contactNumber: String? = null
    internal var contactName: String? = null
    internal var cNote: String? = null
    internal var contactImage: String? = null
    internal lateinit var myCalendar: Calendar
    internal var dbHandler: MyDBHandler? = null
    internal lateinit var intent: Intent
    internal var popup: String? = null
    internal var countText = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_create)

//        Log.e("alarm","ffdfdf"+ Pref.getValue(this, "vibrator", ""))


        dbHandler = MyDBHandler(this@ReminderCreate)
//        btnDate = findViewById<View>(R.id.btnReminderSetDate) as Button
//        btnTime = findViewById<View>(R.id.btnReminderSetTime) as Button
//        btnReminderClose = findViewById<View>(R.id.btnReminderClose) as Button
//        btnSave = findViewById<View>(R.id.btnReminderSave) as Button
//        btnReminderSelectContact = findViewById<View>(R.id.btnReminderSelectContact) as Button
//        txtReminderSelectContact = findViewById<View>(R.id.txtReminderSelectContact) as TextView
//        txtReminderSetDate = findViewById<View>(R.id.txtReminderSetDate) as TextView
//        txtReminderSetTime = findViewById<View>(R.id.txtReminderSetTime) as TextView
//        txtReminderNoteLimit = findViewById<View>(R.id.txtReminderNoteLimit) as TextView
//        edtReminderWriteNote = findViewById<View>(R.id.edtReminderWriteNote) as EditText
        edtReminderWriteNote.imeOptions = EditorInfo.IME_ACTION_DONE
        edtReminderWriteNote.setRawInputType(InputType.TYPE_CLASS_TEXT)


        myCalendar = Calendar.getInstance()
        intent = getIntent()
        popup = intent.getStringExtra("popup")


        txtReminderSelectContact.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                startActivity(Intent(applicationContext, ContactList::class.java))
            }
        })

        btnReminderSave.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                var date1: Date? = null
                val dateFormat = SimpleDateFormat("yyyy/MM/dd hh:mm a")
                val date = Date()
                val Date = dateFormat.format(date).toString()
                val cDate = txtReminderSetDate.text.toString()
                val cTime = txtReminderSetTime.text.toString()
                val compareDateTime = "" + cDate + " " + cTime
                val dateSpecified = SimpleDateFormat("yyyy/MM/dd hh:mm a")
                try {
                    date1 = dateSpecified.parse(compareDateTime)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                Log.d("CallHelper", "ReminderCreate==> " + "Current Date:$Date Compare Date:$date1")
                if (txtReminderSelectContact.text.toString() == "") {
                    Toast.makeText(this@ReminderCreate, "Please Enter Number", Toast.LENGTH_SHORT).show()
                } else if (txtReminderSetDate.text.toString() == "") {
                    Toast.makeText(this@ReminderCreate, "Please Enter Set Date", Toast.LENGTH_SHORT).show()
                } else if (txtReminderSetTime.text.toString() == "") {
                    Toast.makeText(this@ReminderCreate, "Please Enter Set Time", Toast.LENGTH_SHORT).show()
                } else if (edtReminderWriteNote.text.toString() == "") {
                    Toast.makeText(this@ReminderCreate, "Please Enter Set Note", Toast.LENGTH_SHORT).show()
                } else if (date1!!.before(date)) {
                    Toast.makeText(this@ReminderCreate, "Please Enter Set perfect Date And Time", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        addReminder()
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                    txtReminderSelectContact.text = ""
                    txtReminderSetDate.text = ""
                    txtReminderSetTime.text = ""
                    edtReminderWriteNote.setText("")
                    finish()
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                }
            }
        })

        btnReminderClose.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                txtReminderSelectContact.text = ""
                txtReminderSetDate.text = ""
                txtReminderSetTime.text = ""
                edtReminderWriteNote.setText("")
                finish()
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
        })

        val time = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            updateTime()
        }

        txtReminderSetDate.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val dialog = DatePickerDialog(this@ReminderCreate, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    // TODO Auto-generated method stub
                    val mon = month + 1
                    txtReminderSetDate.text = "$year/$mon/$dayOfMonth"
                }, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                dialog.datePicker.minDate = System.currentTimeMillis()
                dialog.show()

            }
        })

        txtReminderSetTime.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                TimePickerDialog(this@ReminderCreate, time, myCalendar
                        .get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false).show()
            }
        })

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                //text Erase then effect
                countText = countText - count
                txtReminderNoteLimit.text = countText.toString() + "/200"
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //text Add then effect
                countText = count + countText
                if (countText == 200) {
                    Toast.makeText(this@ReminderCreate, "Note Write Only 200 letters", Toast.LENGTH_SHORT).show()
                }
                txtReminderNoteLimit.text = countText.toString() + "/200"
            }

            override fun afterTextChanged(s: Editable) {

            }
        }
        edtReminderWriteNote.addTextChangedListener(textWatcher)
        txtReminderNoteLimit.text = countText.toString() + "/200"
    }

    override fun onStart() {
        super.onStart()

        contactNumber = Pref.getValue(this@ReminderCreate, "contactNumber1", null)
        contactName = Pref.getValue(this@ReminderCreate, "contactName1", null)
        contactImage = Pref.getValue(this@ReminderCreate, "contactImage1", null)

        if (contactName != null && contactNumber != null)
            if (contactName!!.length > 0 && contactNumber!!.length > 0)
                txtReminderSelectContact.text = "$contactName($contactNumber)"
            else
                txtReminderSelectContact.text = ""

        if (popup != null && popup!!.equals("popup", ignoreCase = true)) {
            contactNumber = intent.getStringExtra("contactNumber")
            contactName = intent.getStringExtra("contactName")
            txtReminderSelectContact.text = "$contactName($contactNumber)"
            btnReminderSelectContact.visibility = View.GONE
            txtReminderSelectContact.isClickable = false
        }
    }

    private fun updateTime() {
        val myFormat = "hh:mm a" //In which you need put here
        val sdf = SimpleDateFormat(myFormat)
        txtReminderSetTime.text = sdf.format(myCalendar.time)
    }

    @Throws(ParseException::class)
    fun addReminder() {
        cNote = edtReminderWriteNote.text.toString()
        contactNumber = contactNumber!!.replace(" ", "")
        contactNumber = contactNumber!!.replace("+91", "")

        val bean = contactNote()

        bean.contactNumber = contactNumber
        bean.contactName = contactName
        bean.contactNote = cNote
        bean.contactNoteDateTime = CommonUtils.getDateTime()
        bean.time = txtReminderSetTime.text.toString()
        bean.date = txtReminderSetDate.text.toString()
        bean.contactImagePath = contactImage
        bean.type = "reminder"
        Log.d("CallHelper", "ReminderCreate==> " + " Number " + bean.contactNumber
                + " Name " + bean.contactName
                + " Note " + bean.contactNote
                + " DATETIME " + bean.contactNoteDateTime
                + " DATE " + bean.date
                + " TIME " + bean.time
                + "ImagePath" + bean.contactImagePath)
        dbHandler!!.addNote(bean)


        val date = CommonUtils.stringTODate(bean.time, "hh:mm a")
        val calNow = Calendar.getInstance()
        val calSet = calNow.clone() as Calendar
        calSet.set(Calendar.HOUR_OF_DAY, date.hours)
        calSet.set(Calendar.MINUTE, date.minutes)
        calSet.set(Calendar.SECOND, 0)
        calSet.set(Calendar.MILLISECOND, 0)

        if (calSet.compareTo(calNow) <= 0) {
            // Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1)
        }
        var bean1 = contactNote()
        bean1 = dbHandler!!.numberName()
        val notificationId = bean1.contactId

        AlarmUtils.addAlarm(applicationContext, notificationId, calSet, contactNumber, contactName, cNote)
    }


}
