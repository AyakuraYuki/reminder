package cc.ayakurayuki.reminder.bean

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import cc.ayakurayuki.reminder.MainActivity
import cc.ayakurayuki.reminder.service.AlertBroadcastReceiver
import java.io.Serializable
import java.util.*

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class AlarmBean : Serializable {

    var id: Int = 0
    var title: String? = null
    var allDay: Int = 0
    var vibrate: Int = 0
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var startTimeHour: Int = 0
    var startTimeMinute: Int = 0
    var endTimeHour: Int = 0
    var endTimeMinute: Int = 0
    var alarmTime: String? = null
    var alarmColor: String? = null
    var alarmTonePath: String? = null
    var local: String? = null
    var description: String? = null
    var replay: String? = null

    fun isAllDay(): Boolean {
        return this.allDay == 1
    }

    fun setAllDay() {
        this.allDay = 1
    }

    fun unsetAllDay() {
        this.allDay = 0
    }

    fun isVibrate(): Boolean {
        return this.vibrate == 1
    }

    fun setVibrate() {
        this.vibrate = 1
    }

    fun unsetVibrate() {
        this.vibrate = 0
    }

    fun getRealAlarmTime(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        calendar.set(Calendar.HOUR_OF_DAY, startTimeHour)
        calendar.set(Calendar.MINUTE, startTimeMinute)
        calendar.set(Calendar.SECOND, 0)
        when (alarmTime) {
            AlarmTimeEnum.NONE.title -> {
            }
            AlarmTimeEnum.TEN_MINUTE.title -> {
                calendar.add(AlarmTimeEnum.TEN_MINUTE.calendarType, AlarmTimeEnum.TEN_MINUTE.time)
            }
            AlarmTimeEnum.HOUR.title -> {
                calendar.add(AlarmTimeEnum.HOUR.calendarType, AlarmTimeEnum.HOUR.time)
            }
            AlarmTimeEnum.DAY.title -> {
                calendar.add(AlarmTimeEnum.DAY.calendarType, AlarmTimeEnum.DAY.time)
            }
        }
        return calendar
    }

    fun getReplayTime(): Long {
        return when (replay) {
            ReplayTimeEnum.NONE.title -> ReplayTimeEnum.NONE.time
            ReplayTimeEnum.DAILY.title -> ReplayTimeEnum.DAILY.time
            ReplayTimeEnum.WEEKLY.title -> ReplayTimeEnum.WEEKLY.time
            ReplayTimeEnum.YEARLY.title -> ReplayTimeEnum.YEARLY.time
            else -> 0
        }
    }

    fun getCurrentTime(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
    }

    fun schedule(context: Context) {
        val broadcastIntent = Intent(context, AlertBroadcastReceiver::class.java)
        broadcastIntent.putExtra("id", id)
        broadcastIntent.putExtra(MainActivity.extraName, this)
        val sender = PendingIntent.getBroadcast(context, id, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getRealAlarmTime().timeInMillis, getReplayTime(), sender)
    }

    override fun toString(): String {
        return "AlarmBean(id=$id, title=$title, allDay=$allDay, vibrate=$vibrate, year=$year, month=$month, day=$day, startTimeHour=$startTimeHour, startTimeMinute=$startTimeMinute, endTimeHour=$endTimeHour, endTimeMinute=$endTimeMinute, alarmTime=$alarmTime, alarmColor=$alarmColor, alarmTonePath=$alarmTonePath, local=$local, description=$description, replay=$replay)"
    }

}