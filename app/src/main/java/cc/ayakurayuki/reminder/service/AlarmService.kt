package cc.ayakurayuki.reminder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import cc.ayakurayuki.reminder.bean.AlarmBean
import cc.ayakurayuki.reminder.database.DBSupport
import java.util.*

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class AlarmService : Service() {

    companion object {
        private val tag = AlarmService::class.java.name
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "$tag: created")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(tag, "$tag: binding...")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmBean: AlarmBean? = getNext()
        alarmBean?.schedule(applicationContext)
        return START_NOT_STICKY
    }

    private fun getNext(): AlarmBean? {
        val dbSupport = DBSupport(applicationContext)
        val all = dbSupport.all()

        if (all.isEmpty()) {
            return null
        }

        Collections.sort(all) { lhs, rhs ->
            if (lhs.getRealAlarmTime().timeInMillis > rhs.getRealAlarmTime().timeInMillis) {
                1
            } else {
                -1
            }
        }

        return all.indices.firstOrNull {
            all[it].getRealAlarmTime().timeInMillis - all[it].getCurrentTime().timeInMillis > 0
        }?.let { all[it] }
    }

}