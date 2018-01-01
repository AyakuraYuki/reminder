package cc.ayakurayuki.reminder.service

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Vibrator
import android.view.WindowManager
import cc.ayakurayuki.reminder.bean.AlarmBean
import cc.ayakurayuki.reminder.database.DBSupport

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class AlertBroadcastReceiver : BroadcastReceiver() {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private lateinit var vibrator: Vibrator
    private var isVibrator: Boolean = false

    override fun onReceive(context: Context, intent: Intent?) {
        val receiver = Intent(context, AlarmServiceBroadcastReceiver::class.java)
        context.sendBroadcast(receiver)
        val bean = get(context, intent!!.extras["id"].toString())
        showAlarmDialog(context, bean)
    }

    private fun showAlarmDialog(context: Context, bean: AlarmBean) {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        playMusicAndVibrate(context, bean)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("距离您定的日期 ${bean.title} 已经到了哦！")
                .setMessage(bean.description)
                .setPositiveButton("我知道了", { _, _ ->
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    mediaPlayer = MediaPlayer()
                    if (isVibrator) {
                        vibrator.cancel()
                        isVibrator = false
                    }
                })

        val dialog = builder.create()
        dialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()
    }

    private fun playMusicAndVibrate(context: Context, bean: AlarmBean) {
        val ringtoneUri = Uri.parse(bean.alarmTonePath)
        if (mediaPlayer.isPlaying)
            mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setVolume(100f, 100f)
        mediaPlayer.setDataSource(context, ringtoneUri)
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM)
        mediaPlayer.isLooping = true
        mediaPlayer.prepare()
        mediaPlayer.start()
        if (bean.vibrate == 1) {
            vibrator.vibrate(longArrayOf(1000, 50, 1000, 50), 0)
            isVibrator = true
        }
    }

    private fun get(context: Context, id: String): AlarmBean {
        val dbSupport = DBSupport(context)
        return dbSupport.get(id.toInt())
    }

}