package com.greenapex.callhelper.dbCallHelper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.text.TextUtils

import com.greenapex.callhelper.Model.contactNote

import java.util.ArrayList

/**
 * Created by GreenApex on 30/11/17.
 */

class MyDBHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val query = (" CREATE TABLE " + TABLE_NAME + "("
                + Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CONTACT_NUMBER + " TEXT,"
                + CONTACT_NOTE + " TEXT,"
                + CONTACT_NAME + " TEXT,"
                + CONTACT_IMAGE + " TEXT,"
                + CONTACT_NOTE_DATETIME + " DATETIME DEFAULT (datetime('now','localtime')),"
                + REMINDER_CONTACT_DATE + " TEXT,"
                + REMINDER_CONTACT_TIME + " TEXT,"
                + TYPE + " TEXT" + ");")
        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addNote(bean: contactNote) {
        val contentValues = ContentValues()
        contentValues.put(CONTACT_NUMBER, bean.contactNumber)
        contentValues.put(CONTACT_NAME, bean.contactName)
        contentValues.put(CONTACT_NOTE, bean.contactNote)
        contentValues.put(CONTACT_IMAGE, bean.contactImagePath)
        contentValues.put(CONTACT_NOTE_DATETIME, bean.contactNoteDateTime)
        contentValues.put(REMINDER_CONTACT_TIME, bean.time)
        contentValues.put(REMINDER_CONTACT_DATE, bean.date)
        contentValues.put(TYPE, bean.type)


        val db = writableDatabase
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }

    fun selectAll(value: String): List<contactNote> {

        val runBeanList = ArrayList<contactNote>()
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $TYPE LIKE '$value' ORDER BY $Id DESC "
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.count > 0) {

            if (cursor.isBeforeFirst) {
                while (cursor.moveToNext()) {
                    val bean = contactNote()
                    bean.contactId = cursor.getInt(cursor.getColumnIndex(Id))
                    bean.contactName = cursor.getString(cursor.getColumnIndex(CONTACT_NAME))
                    bean.contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER))
                    bean.contactNote = cursor.getString(cursor.getColumnIndex(CONTACT_NOTE))

                    val abc = cursor.getString(cursor.getColumnIndex(CONTACT_IMAGE))

                    if (TextUtils.isEmpty(abc)) {
                        bean.contactImagePath = cursor.getString(cursor.getColumnIndex(CONTACT_IMAGE))
                    } else {

                    }
                    bean.contactNoteDateTime = cursor.getString(cursor.getColumnIndex(CONTACT_NOTE_DATETIME))
                    bean.time = cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_TIME))
                    bean.date = cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_DATE))
                    bean.type = cursor.getString(cursor.getColumnIndex(TYPE))
                    runBeanList.add(bean)
                }
            }
        }
        return runBeanList
    }

    fun deleteNote(value: Int) {
        val database = this.writableDatabase
        val deleteQuery = " DELETE FROM $TABLE_NAME WHERE $Id = $value"
        database.execSQL(deleteQuery)
        database.close()
    }

    fun updateNote(Note: String, Dt: String, value: Int) {
        val database = this.writableDatabase
        val updateQuery = " UPDATE $TABLE_NAME set $CONTACT_NOTE = '$Note',$CONTACT_NOTE_DATETIME = '$Dt' WHERE $Id = $value"
        database.execSQL(updateQuery)
        database.close()
    }

    fun singleNote(value: Int): contactNote {

        val runBeanList = contactNote()

        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, Id + "=?", arrayOf(value.toString()), null, null, null)
        if (cursor != null) {

            if (cursor.isBeforeFirst) {
                while (cursor.moveToNext()) {

                    val Note = cursor.getString(cursor.getColumnIndex(CONTACT_NOTE))
                    val contactName = cursor.getString(cursor.getColumnIndex(CONTACT_NAME))
                    val contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER))

                    runBeanList.contactNote = Note
                    runBeanList.contactName = contactName
                    runBeanList.contactNumber = contactNumber
                }
            }
        }

        return runBeanList
    }

    fun noteReminder(value: String): List<contactNote> {

        val runBeanList = ArrayList<contactNote>()

        val query = " SELECT * FROM $TABLE_NAME WHERE $CONTACT_NUMBER = '$value' ORDER BY $Id DESC "
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor != null) {
            if (cursor.isBeforeFirst) {
                while (cursor.moveToNext()) {
                    val bean = contactNote()
                    bean.contactId = cursor.getInt(cursor.getColumnIndex(Id))
                    bean.contactName = cursor.getString(cursor.getColumnIndex(CONTACT_NAME))
                    bean.contactNumber = cursor.getString(cursor.getColumnIndex(CONTACT_NUMBER))
                    bean.contactNote = cursor.getString(cursor.getColumnIndex(CONTACT_NOTE))
                    bean.contactNoteDateTime = cursor.getString(cursor.getColumnIndex(CONTACT_NOTE_DATETIME))
                    bean.time = cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_TIME))
                    bean.date = cursor.getString(cursor.getColumnIndex(REMINDER_CONTACT_DATE))
                    bean.type = cursor.getString(cursor.getColumnIndex(TYPE))
                    runBeanList.add(bean)
                }
            }
        }
        return runBeanList
    }

    fun numberName(): contactNote {

        val runBeanList = contactNote()
        val db = this.readableDatabase
        val selectQuery = " SELECT * FROM $TABLE_NAME ORDER BY $Id DESC LIMIT 1 "
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.isBeforeFirst) {
                while (cursor.moveToNext()) {
                    val Id1 = cursor.getInt(cursor.getColumnIndex(Id))
                    runBeanList.contactId = Id1
                }
            }
        }
        return runBeanList
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "callHelper.db"
        private val TABLE_NAME = "call_note_reminder"
        private val Id = "note_reminder_id"
        private val CONTACT_NUMBER = "contact_number"
        private val CONTACT_NAME = "contact_name"
        private val CONTACT_NOTE = "contact_note"
        private val CONTACT_NOTE_DATETIME = "contact_note_datetime"
        private val REMINDER_CONTACT_DATE = "reminder_date"
        private val REMINDER_CONTACT_TIME = "reminder_time"
        private val CONTACT_IMAGE = "contact_image"
        private val TYPE = "type"
    }
}
