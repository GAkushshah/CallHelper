package com.greenapex.callhelper

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast

import com.greenapex.callhelper.Activity.Setting
import com.greenapex.callhelper.Fragment.CallLog
import com.greenapex.callhelper.Fragment.CallNote
import com.greenapex.callhelper.Fragment.ContactList
import com.greenapex.callhelper.Fragment.Reminder
import com.greenapex.callhelper.Util.Pref
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_toolbar.*
import outlander.showcaseview.ShowcaseViewBuilder

class MainActivity : AppCompatActivity() {

    //internal var bottomNavigationView: BottomNavigationView
    internal var doubleBackToExitPressedOnce = false
    // internal var btnSetting: Button
    internal lateinit var toolbar: Toolbar
    internal lateinit var alarmManager: AlarmManager
    var showcaseViewBuilder: ShowcaseViewBuilder? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showcaseViewBuilder = ShowcaseViewBuilder.init(this)

        if ((Pref.getValue(this@MainActivity, "strShowcase", "").equals(""))) {
            showcaseSettingBtn()
        } else {
            Log.d("Done", "Done")
        }

        toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)


        //  btnSetting = findViewById<View>(R.id.btnSetting) as Button
        //mAdapter = new adpterContact(this, mItems);
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        btnSetting.setOnClickListener {

            startActivity(Intent(applicationContext, Setting::class.java))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
        }


        iv1.setOnClickListener {
            iv2.isEnabled = true
            iv3.isEnabled = true
            iv4.isEnabled = true
            var selectedFragment = supportFragmentManager.findFragmentById(R.id.frame)
            if (selectedFragment is CallLog) {
                iv1.isEnabled = false
            } else {
                selectedFragment = CallLog.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, selectedFragment)
                transaction.commit()
                iconChange(1)

            }

        }
        iv2.setOnClickListener {
            iv1.isEnabled = true
            iv3.isEnabled = true
            iv4.isEnabled = true
            var selectedFragment = supportFragmentManager.findFragmentById(R.id.frame)
            if (selectedFragment is ContactList) {
                iv2.isEnabled = false
            } else {
                selectedFragment = ContactList.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, selectedFragment)
                transaction.commit()
                iconChange(2)
            }

        }
        iv3.setOnClickListener {
            iv1.isEnabled = true
            iv2.isEnabled = true
            iv4.isEnabled = true
            var selectedFragment = supportFragmentManager.findFragmentById(R.id.frame)
            if (selectedFragment is CallNote) {
                iv3.isEnabled = false
            } else {
                selectedFragment = CallNote.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, selectedFragment)
                transaction.commit()
                iconChange(3)

            }

        }
        iv4.setOnClickListener {
            iv1.isEnabled = true
            iv2.isEnabled = true
            iv3.isEnabled = true
            var selectedFragment = supportFragmentManager.findFragmentById(R.id.frame)
            if (selectedFragment is Reminder) {
                iv4.isEnabled = false
            } else {
                selectedFragment = Reminder.newInstance()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.frame, selectedFragment)
                transaction.commit()
                iconChange(4)
            }

        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, CallLog.newInstance())
        transaction.commit()
        iconChange(1)
    }

//    internal object BottomNavigationViewHelper {
//
//        @SuppressLint("RestrictedApi")
//        fun removeShiftMode(view: BottomNavigationView) {
//            val menuView = view.getChildAt(0) as BottomNavigationMenuView
//            try {
//                val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
//                shiftingMode.isAccessible = true
//                shiftingMode.setBoolean(menuView, false)
//                shiftingMode.isAccessible = false
//                for (i in 0 until menuView.childCount) {
//                    val item = menuView.getChildAt(i) as BottomNavigationItemView
//                    item.setShiftingMode(false)
//                    // set once again checked value, so view will be updated
//                    item.setChecked(item.itemData.isChecked)
//                }
//            } catch (e: NoSuchFieldException) {
//                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field")
//            } catch (e: IllegalAccessException) {
//                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode")
//            }
//
//        }
//    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    private fun showcaseSettingBtn() {
        showcaseViewBuilder!!.setTargetView(btnSetting)
                .setBackgroundOverlayColor(-0x11b2b2b3)
                .setRingColor(Color.parseColor("#e0c907"))
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()))
                .addCustomView(R.layout.setting_description_view, Gravity.BOTTOM)
                .setCustomViewMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics()).toInt())
        showcaseViewBuilder!!.show()
        showcaseViewBuilder!!.setClickListenerOnView(R.id.btn, object : View.OnClickListener {
            override fun onClick(v: View) {
                showcaseViewBuilder!!.hide()
                showcaseBottom()
            }
        })
    }

    private fun showcaseBottom() {

        showcaseViewBuilder!!.setTargetView(iv2)
                .setBackgroundOverlayColor(-0x11b2b2b3)
                .setRingColor(Color.parseColor("#e0c907"))
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()))
                .addCustomView(R.layout.contact_description_view, Gravity.TOP)
                .setCustomViewMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics()).toInt())
        showcaseViewBuilder!!.show()
        showcaseViewBuilder!!.setClickListenerOnView(R.id.btn, object : View.OnClickListener {
            override fun onClick(v: View) {
                showcaseViewBuilder!!.hide()
                showcaseBottom1()
            }
        })
    }

    private fun showcaseBottom1() {

        showcaseViewBuilder!!.setTargetView(iv3)
                .setBackgroundOverlayColor(-0x11b2b2b3)
                .setRingColor(Color.parseColor("#e0c907"))
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()))
                .addCustomView(R.layout.note_description_view, Gravity.TOP)
                .setCustomViewMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics()).toInt())
        showcaseViewBuilder!!.show()
        showcaseViewBuilder!!.setClickListenerOnView(R.id.btn, object : View.OnClickListener {
            override fun onClick(v: View) {
                showcaseViewBuilder!!.hide()
                showcaseBottom2()

            }
        })
    }

    private fun showcaseBottom2() {

        showcaseViewBuilder!!.setTargetView(iv4)
                .setBackgroundOverlayColor(-0x11b2b2b3)
                .setRingColor(Color.parseColor("#e0c907"))
                .setRingWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, getResources().getDisplayMetrics()))
                .setMarkerDrawable(getResources().getDrawable(android.R.drawable.arrow_down_float), Gravity.BOTTOM)
                .setDrawableLeftMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, getResources().getDisplayMetrics()))
                .addCustomView(R.layout.reminder_description_view, Gravity.TOP)
                .setCustomViewMargin(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, getResources().getDisplayMetrics()).toInt())
        showcaseViewBuilder!!.show()
        showcaseViewBuilder!!.setClickListenerOnView(R.id.btn, object : View.OnClickListener {
            override fun onClick(v: View) {
                Pref.setValue(this@MainActivity, "strShowcase", "notActive")
                showcaseViewBuilder!!.hide()
            }
        })
    }

    fun iconChange(i: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.calllhistory, getApplicationContext().getTheme()));
        } else {
            iv1.setImageDrawable(getResources().getDrawable(R.drawable.calllhistory));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.contact_notactive, getApplicationContext().getTheme()));
        } else {
            iv2.setImageDrawable(getResources().getDrawable(R.drawable.contact_notactive));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.callnote, getApplicationContext().getTheme()));
        } else {
            iv3.setImageDrawable(getResources().getDrawable(R.drawable.callnote));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv4.setImageDrawable(getResources().getDrawable(R.drawable.reminder, getApplicationContext().getTheme()));
        } else {
            iv4.setImageDrawable(getResources().getDrawable(R.drawable.reminder));
        }


        if (i == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv1.setImageDrawable(getResources().getDrawable(R.drawable.calllhistoryactive, getApplicationContext().getTheme()));
            } else {
                iv1.setImageDrawable(getResources().getDrawable(R.drawable.calllhistoryactive));
            }
        } else if (i == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv2.setImageDrawable(getResources().getDrawable(R.drawable.contactactive, getApplicationContext().getTheme()));
            } else {
                iv2.setImageDrawable(getResources().getDrawable(R.drawable.contactactive));
            }
        } else if (i == 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv3.setImageDrawable(getResources().getDrawable(R.drawable.callnoteactive, getApplicationContext().getTheme()));
            } else {
                iv3.setImageDrawable(getResources().getDrawable(R.drawable.callnoteactive));
            }
        } else if (i == 4) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv4.setImageDrawable(getResources().getDrawable(R.drawable.reminderactive, getApplicationContext().getTheme()));
            } else {
                iv4.setImageDrawable(getResources().getDrawable(R.drawable.reminderactive));
            }
        }

    }

}
