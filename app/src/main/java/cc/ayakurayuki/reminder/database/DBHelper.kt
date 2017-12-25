package cc.ayakurayuki.reminder.database

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import cc.ayakurayuki.reminder.database.schema.Schema
import cc.ayakurayuki.reminder.database.schema.table.Alarm

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class DBHelper : SQLiteOpenHelper {

    companion object {
        private val tag: String = DBHelper::class.java.name
    }

    constructor(context: Context) : super(context, Schema.filename, null, Schema.databaseVersion)

    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int)
            : super(context, name, factory, version)

    constructor(context: Context, name: String, factory: SQLiteDatabase.CursorFactory, version: Int, errorHandler: DatabaseErrorHandler)
            : super(context, name, factory, version, errorHandler)

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            create table ${Alarm.tableName}(
                ${Alarm.Column.id}              integer primary key autoincrement,
                ${Alarm.Column.title}           text,
                ${Alarm.Column.allDay}          int(20),
                ${Alarm.Column.vibrate}         int(20),
                ${Alarm.Column.year}            int(20),
                ${Alarm.Column.month}           int(20),
                ${Alarm.Column.day}             int(20),
                ${Alarm.Column.startTimeHour}   int(20),
                ${Alarm.Column.startTimeMinute} int(20),
                ${Alarm.Column.endTimeHour}     int(20),
                ${Alarm.Column.endTimeMinute}   int(20),
                ${Alarm.Column.alarmTime}       text,
                ${Alarm.Column.alarmColor}      text,
                ${Alarm.Column.alarmTonePath}   text,
                ${Alarm.Column.local}           text,
                ${Alarm.Column.description}     text,
                ${Alarm.Column.replay}          text
            )
        """.trimIndent())
        Log.d(tag, "$tag: table ${Alarm.tableName} created.")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("""
            drop table if exists ${Alarm.tableName}
        """.trimIndent())
        Log.d(tag, "$tag: drop table ${Alarm.tableName}, process upgrade.")
        onCreate(db)
    }

}