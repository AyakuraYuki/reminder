package cc.ayakurayuki.reminder.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import cc.ayakurayuki.reminder.bean.AlarmBean
import cc.ayakurayuki.reminder.database.schema.table.Alarm
import java.util.*

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class DBSupport {

    private var context: Context
    private var dbHelper: DBHelper
    private var db: SQLiteDatabase

    constructor(context: Context) {
        this.context = context
        this.dbHelper = DBHelper(this.context)
        this.db = dbHelper.readableDatabase
    }

    /**
     * 通过ID获取对象
     */
    fun get(id: Int): AlarmBean {
        val cursor = getCursor(id)
        cursor.moveToNext()
        return getBean(cursor)
    }

    /**
     * 获取全部
     */
    fun all(): List<AlarmBean> {
        val list = arrayListOf<AlarmBean>()
        val cursor = getCursor()
        if (cursor.moveToFirst()) {
            do {
                list.add(getBean(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    /**
     * 根据日期查询
     */
    fun listByDate(calendar: Calendar): List<AlarmBean> {
        val list = arrayListOf<AlarmBean>()
        val cursor = getCursor(calendar)
        if (cursor.moveToFirst()) {
            do {
                list.add(getBean(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    /**
     * 保存
     * 根据ID判断是否为新条目
     * ID = 0: 插入
     * ID != 0: 修改
     */
    fun save(alarmBean: AlarmBean) {
        if (alarmBean.id == 0) {
            insert(alarmBean)
        } else {
            update(alarmBean)
        }
    }

    /**
     * 删除
     */
    fun delete(id: Int) {
        db.delete(Alarm.tableName, "${Alarm.Column.id}=?", arrayOf(id.toString()))
    }

    fun close() {
        db.close()
        dbHelper.close()
    }

    /**
     * 插入条目
     */
    private fun insert(alarmBean: AlarmBean) {
        val values = getContentValues(alarmBean)
        db.insert(Alarm.tableName, Alarm.Column.title, values)
    }

    /**
     * 修改条目
     */
    private fun update(alarmBean: AlarmBean) {
        val values = getContentValues(alarmBean)
        db.update(Alarm.tableName, values, "${Alarm.Column.id}=?", arrayOf(alarmBean.id.toString()))
    }

    private fun getCursor(): Cursor {
        val columns = arrayOf(
                Alarm.Column.id,
                Alarm.Column.title,
                Alarm.Column.allDay,
                Alarm.Column.vibrate,
                Alarm.Column.year,
                Alarm.Column.month,
                Alarm.Column.day,
                Alarm.Column.startTimeHour,
                Alarm.Column.startTimeMinute,
                Alarm.Column.endTimeHour,
                Alarm.Column.endTimeMinute,
                Alarm.Column.alarmTime,
                Alarm.Column.alarmColor,
                Alarm.Column.alarmTonePath,
                Alarm.Column.local,
                Alarm.Column.description,
                Alarm.Column.replay
        )
        return db.query(Alarm.tableName, columns, null, null, null, null, null)
    }

    private fun getCursor(calendar: Calendar): Cursor {
        return db.rawQuery(
                """
                        select *
                        from ${Alarm.tableName}
                        where ${Alarm.Column.year}=?
                            and ${Alarm.Column.month}=?
                            and ${Alarm.Column.day}=?
                    """.trimIndent(),
                arrayOf(
                        calendar.get(Calendar.YEAR).toString(),
                        calendar.get(Calendar.MONTH).toString(),
                        calendar.get(Calendar.DAY_OF_MONTH).toString()
                ))
    }

    private fun getCursor(id: Int): Cursor {
        return db.rawQuery(
                """
                    select *
                    from ${Alarm.tableName}
                    where ${Alarm.Column.id}=?
                """.trimIndent(),
                arrayOf(
                        id.toString()
                )
        )
    }

    private fun getBean(cursor: Cursor): AlarmBean {
        return AlarmBean().apply {
            id = cursor.getInt(0)
            title = cursor.getString(1)
            allDay = cursor.getInt(2)
            vibrate = cursor.getInt(3)
            year = cursor.getInt(4)
            month = cursor.getInt(5)
            day = cursor.getInt(6)
            startTimeHour = cursor.getInt(7)
            startTimeMinute = cursor.getInt(8)
            endTimeHour = cursor.getInt(9)
            endTimeMinute = cursor.getInt(10)
            alarmTime = cursor.getString(11)
            alarmColor = cursor.getString(12)
            alarmTonePath = cursor.getString(13)
            local = cursor.getString(14)
            description = cursor.getString(15)
            replay = cursor.getString(16)
        }
    }

    private fun getContentValues(alarmBean: AlarmBean): ContentValues {
        return ContentValues().apply {
            put(Alarm.Column.title, alarmBean.title)
            put(Alarm.Column.allDay, alarmBean.allDay)
            put(Alarm.Column.vibrate, alarmBean.vibrate)
            put(Alarm.Column.year, alarmBean.year)
            put(Alarm.Column.month, alarmBean.month)
            put(Alarm.Column.day, alarmBean.day)
            put(Alarm.Column.startTimeHour, alarmBean.startTimeHour)
            put(Alarm.Column.startTimeMinute, alarmBean.startTimeMinute)
            put(Alarm.Column.endTimeHour, alarmBean.endTimeHour)
            put(Alarm.Column.endTimeMinute, alarmBean.endTimeMinute)
            put(Alarm.Column.alarmTime, alarmBean.alarmTime)
            put(Alarm.Column.alarmColor, alarmBean.alarmColor)
            put(Alarm.Column.alarmTonePath, alarmBean.alarmTonePath)
            put(Alarm.Column.local, alarmBean.local)
            put(Alarm.Column.description, alarmBean.description)
            put(Alarm.Column.replay, alarmBean.replay)
        }
    }

}