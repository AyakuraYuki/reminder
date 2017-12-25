package cc.ayakurayuki.reminder.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class AlarmServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmService = Intent(context, AlarmService::class.java)
        context!!.startService(alarmService)
    }
}