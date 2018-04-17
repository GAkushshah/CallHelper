package com.greenapex.callhelper.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log

import com.greenapex.callhelper.MainActivity
import com.greenapex.callhelper.R
import com.greenapex.callhelper.Util.Pref

import java.util.ArrayList
import java.util.HashMap

class SplashScreen : AppCompatActivity() {
    private val TAG = "CallHelper"
    internal var showcase: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (checkAndRequestPermissions()) {

            Handler().postDelayed(/*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
                    {
                        val i = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(i)
                        // close this activity
                        finish()
                    }, SPLASH_TIME_OUT.toLong())
        }

    }

    private fun checkAndRequestPermissions(): Boolean {

        val camerapermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()

        if (camerapermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
        }
        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        Log.d(TAG, "Permission callback called-------")
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {

                val perms = HashMap<String, Int>()
                // Initialize the map with both permissions
                perms.put(Manifest.permission.READ_CALL_LOG, PackageManager.PERMISSION_GRANTED)
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED)
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED)
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED)
                // Fill with actual results from user
                if (grantResults.size > 0) {
                    for (i in permissions.indices)
                        perms.put(permissions[i], grantResults[i])
                    // Check for both permissions
                    if (perms[Manifest.permission.READ_CALL_LOG] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.READ_CONTACTS] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.READ_PHONE_STATE] == PackageManager.PERMISSION_GRANTED
                            && perms[Manifest.permission.CALL_PHONE] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "sms & location services permission granted")
                        // process the normal flow
                        val i = Intent(this@SplashScreen, MainActivity::class.java)
                        startActivity(i)

                        // close this activity
                        finish()

                        //else any one or both the permissions are not granted
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ")
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                            showDialogOK("Service Permissions are required for this app",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        when (which) {
                                            DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                                            DialogInterface.BUTTON_NEGATIVE ->
                                                // proceed with logic by disabling the related features or quit the app.
                                                finish()
                                        }
                                    })
                        } else {
                            explain("You need to give some mandatory permissions to continue. Do you want to go to app settings?")
                            //proceed with logic by disabling the related features or quit the app.
                        }//permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                    }
                }
            }
        }
    }

    private fun showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show()
    }

    private fun explain(msg: String) {
        val dialog = android.support.v7.app.AlertDialog.Builder(this)
        dialog.setMessage(msg)
                .setPositiveButton("Yes") { paramDialogInterface, paramInt ->
                    //  permissionsclass.requestPermission(type,code);
                    startActivity(Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.exampledemo.parsaniahardik.marshmallowpermission")))
                }
                .setNegativeButton("Cancel") { paramDialogInterface, paramInt -> finish() }
        dialog.show()
    }

    companion object {

        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
        private val SPLASH_TIME_OUT = 5000
    }
}
