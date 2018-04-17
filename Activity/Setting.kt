package com.greenapex.callhelper.Activity

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings.System.DEFAULT_RINGTONE_URI
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.greenapex.callhelper.Adpter.AdapterVibrate
import com.greenapex.callhelper.Interface.IVibrate

import com.greenapex.callhelper.R
import com.greenapex.callhelper.R.id.*
import com.greenapex.callhelper.Util.CommonUtils.context
import com.greenapex.callhelper.Util.OnOneOffClickListener
import com.greenapex.callhelper.Util.Pref
import kotlinx.android.synthetic.main.activity_setting.*

class Setting : BaseActivity() {
    var subjects = arrayOf<String>("off", "Default", "Short", "Long")
    internal var incomingOnOff: Boolean? = null
    internal var outgoingOnOff: Boolean? = null
    internal var callEndOnOff: Boolean? = null
    internal lateinit var adpter1: AdapterVibrate
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        setupToolbar("Setting")
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

        if (!TextUtils.isEmpty(Pref.getValue(this, "vibrator", ""))) {
            val pos = (Pref.getValue(this, "vibrator", "")).toInt()
            val str = subjects[pos]
            txtVibrateName.setText(str)
        } else {
            txtVibrateName.text = "Default"
            Pref.setValue(this, "vibrator", "1")
        }

        if (!TextUtils.isEmpty(Pref.getValue(this,"RingtonesetbyName",""))){
            val ringToneName = (Pref.getValue(this,"RingtonesetbyName",""))
            txtRingtoneName.setText(ringToneName)
        }else{
            txtRingtoneName.setText("Default Ringtone")
        }


        incomingOnOff = Pref.getValue(this, "isOnGoingIncoming", true)
        if (incomingOnOff == true) {
            disPlayNoteIncoming.isChecked = true
        } else {
            disPlayNoteIncoming.isChecked = false
        }
        outgoingOnOff = Pref.getValue(this, "isOnGoingOutgoing", true)
        if (outgoingOnOff == true) {
            disPlayNoteOutgoing.isChecked = true
        } else {
            disPlayNoteOutgoing.isChecked = false
        }
        callEndOnOff = Pref.getValue(this, "isOnGoingCallEnd", true)
        if (callEndOnOff == true) {
            disPlayNoteCallEnd.isChecked = true
        } else {
            disPlayNoteCallEnd.isChecked = false
        }

        disPlayNoteIncoming.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == false) {
                Pref.setValue(this@Setting, "isOnGoingIncoming", isChecked)
            } else {
                Pref.setValue(this@Setting, "isOnGoingIncoming", isChecked)
            }
        }

        disPlayNoteOutgoing.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked == false) {
                Pref.setValue(this@Setting, "isOnGoingOutgoing", isChecked)
            } else {
                Pref.setValue(this@Setting, "isOnGoingOutgoing", isChecked)
            }
        }

        disPlayNoteCallEnd.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.d("Call_Strat", "" + callEndOnOff!!)
            Pref.setValue(this@Setting, "isOnGoingCallEnd", isChecked)

            callEndOnOff = Pref.getValue(this@Setting, "isOnGoingCallEnd", true)
            Log.d("Call_Complete", "" + callEndOnOff!!)
        }

        linearSetSound.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View?) {
                val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION)
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone")
                if (!TextUtils.isEmpty(Pref.getValue(this@Setting, "Ringtoneset", ""))) {
                    Log.e("in", "" + Pref.getValue(this@Setting, "Ringtoneset", ""))
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(Pref.getValue(this@Setting, "Ringtoneset", "")))
                } else {
                    Log.e("in", "Out==")

                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, DEFAULT_RINGTONE_URI)

                }
                startActivityForResult(intent, 5)
            }
        })

        linearSetVibrate.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View?) {
                val recyclerView: RecyclerView

                val dialogBuilder = AlertDialog.Builder(this@Setting)
                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val dialogView = inflater.inflate(R.layout.app_vibrate, null)
                dialogBuilder.setView(dialogView)
                dialog = dialogBuilder.create()
                recyclerView = dialogView.findViewById<View>(R.id.recyclerVibrate) as RecyclerView

                val layoutManager = LinearLayoutManager(this@Setting)
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adpter1 = AdapterVibrate(iVibrate, this@Setting, subjects)
                recyclerView.adapter = adpter1

                dialog!!.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", { dialog: DialogInterface?, which: Int ->
                    dialog!!.dismiss()
                })

                dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
                dialog!!.show()
            }
        })

        relativeAbout.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val intent = Intent(this@Setting, AboutActivity::class.java)
                startActivity(intent)
            }
        })

        relativeFAQ.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val intent = Intent(this@Setting, FAQActivity::class.java)
                startActivity(intent)
            }
        })

        relativeTermsOfUse.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val intent = Intent(this@Setting, TermsOfUseActivity::class.java)
                startActivity(intent)
            }
        })

        relativePrivacyPolicy.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val intent = Intent(this@Setting, PrivacyPolicyActivity::class.java)
                startActivity(intent)
            }
        })

        relativeHelp.setOnClickListener(object : OnOneOffClickListener() {
            override fun onSingleClick(v: View) {
                val intent = Intent(this@Setting, HelpActivity::class.java)
                startActivity(intent)
            }
        })

        setupToolbar("Setting")
    }

    var iVibrate: IVibrate = object : IVibrate {
        override fun yourVibrateMethod(position: Int, title: String?) {
            txtVibrateName.setText(title)
            Pref.setValue(this@Setting, "keyVibrater", title)

            if (dialog!!.isShowing) {
                dialog!!.dismiss()
            }
        }
    }

    protected override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == 5) {
            val uri = intent?.getParcelableExtra<Parcelable>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (uri != null) {
                Pref.setValue(this, "Ringtoneset", "" + uri);
                var ringtone = RingtoneManager.getRingtone(this, uri as Uri?)
                var title = ringtone.getTitle(this)
                Pref.setValue(this, "RingtonesetbyName", "" + title)
                txtRingtoneName.setText(title)

            } else {
                Log.d("RingTone", "Hello")
            }
        }
    }

}
