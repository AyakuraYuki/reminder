package cc.ayakurayuki.reminder.service

import android.app.Activity
import android.content.Intent
import android.util.Log

/**
 * Created by ayakurayuki on 2017/12/28.
 */
class SendAlarmBroadcast {

    companion object {
        private val tag: String = SendAlarmBroadcast::class.java.name

        fun startAlarmService(activity: Activity) {
            val startAlarmServiceIntent = Intent(activity, AlarmServiceBroadcastReceiver::class.java)
            activity.sendBroadcast(startAlarmServiceIntent, null)
            Log.d(tag, "send broadcast: ${startAlarmServiceIntent.dataString}")
        }
    }

}
