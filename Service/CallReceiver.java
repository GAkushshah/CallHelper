package com.greenapex.callhelper.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.greenapex.callhelper.Activity.NoteCreate;
import com.greenapex.callhelper.Activity.PopupActivity;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.Util.Pref;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.util.List;

/**
 * Created by GreenApex on 23/2/18.
 */

public class CallReceiver extends BroadcastReceiver {

    Context context;
    String savedNumber, saveName;
    MyDBHandler dbHandler;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // If it is to call (outgoing)
            dbHandler = new MyDBHandler(context);
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

            if (!TextUtils.isEmpty(savedNumber)) {
                Pref.setValue(context, "PhoneNumber", savedNumber);
            }
            //List<contactNote> list = dbHandler.noteReminder(savedNumber);
            Boolean isOnGoing = Pref.getValue(context, "isOnGoingOutgoing", true);
            if (isOnGoing == true) {
                Intent intent1 = new Intent(context, PopupActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
                Log.d("Call_Outgoing", savedNumber);
            } else {
                Log.d("Call_Outgoing", "NO Popup");
            }
        } else {
            try {
                // TELEPHONY MANAGER class object to register one listner
                TelephonyManager tmgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                //Create Listner
                MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

                // Register listener for LISTEN_CALL_STATE
                tmgr.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            } catch (Exception e) {
                Log.e("Phone Receive Error", " " + e);
            }
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == TelephonyManager.CALL_STATE_RINGING) {
                if (!TextUtils.isEmpty(incomingNumber)) {
                    Pref.setValue(context, "PhoneNumber", incomingNumber);
                }
                Boolean isOnGoing = Pref.getValue(context, "isOnGoingIncoming", true);
                if (isOnGoing == true) {
                    Intent intent = new Intent(context, PopupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    Log.d("Call_Ringing", incomingNumber);
                } else {
                    Log.d("Call_Ringing", "NO Popup");
                }
            } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                if (!TextUtils.isEmpty(incomingNumber)) {
                    Pref.setValue(context, "PhoneNumber", incomingNumber);
                }
                Boolean isOnGoing = Pref.getValue(context, "isOnGoingCallEnd", true);
                if (isOnGoing == true) {
                    Intent intent = new Intent(context, PopupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("IsOn","IsOn");
                    context.startActivity(intent);
                    Log.d("Call_Ending", incomingNumber);
                } else {
                    Log.d("Call_Ending", "NO Popup");
                }
            }
        }
    }
}