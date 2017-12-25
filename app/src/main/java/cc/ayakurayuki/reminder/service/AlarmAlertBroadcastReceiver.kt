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
import java.io.IOException

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class AlarmAlertBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val alarmBundleKey = "alarm"
    }

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator
    private var isVibrator: Boolean = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmServiceBroadcastReceiver = Intent(context, AlarmServiceBroadcastReceiver::class.java)
        context!!.sendBroadcast(alarmServiceBroadcastReceiver, null)
        val bundle = alarmServiceBroadcastReceiver.extras
        val bean: AlarmBean = bundle.getSerializable(alarmBundleKey) as AlarmBean
        showAlarmDialog(context, bean)
    }

    private fun showAlarmDialog(context: Context, bean: AlarmBean) {
        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        playMusicAndVibrate(context, bean)

        val builder = AlertDialog.Builder(context)
        builder.setTitle("距离您定的日期 ${bean.title} 已经到了哦！")
                .setMessage(bean.description)
                .setPositiveButton("我知道了", { _, _ ->
                    if (mediaPlayer != null) {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                        mediaPlayer = MediaPlayer()
                        if (isVibrator) {
                            vibrator.cancel()
                            isVibrator = false
                        }

                    }
                })

        val dialog = builder.create()
        dialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()
    }

    private fun playMusicAndVibrate(context: Context, bean: AlarmBean) {
        val ringtoneUri = Uri.parse(bean.alarmTonePath)
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        } else {
            if (mediaPlayer.isPlaying)
                mediaPlayer.stop()
            mediaPlayer.reset()
        }
        try {
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
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}