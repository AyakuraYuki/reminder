package cc.ayakurayuki.reminder

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import cc.ayakurayuki.reminder.adapter.SetRingtoneAdapter
import cc.ayakurayuki.reminder.adapter.SetRingtoneAdapter.OnRecyclerViewItemClickListener

class SetRingtoneActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var buttonCloseActivity: ImageButton
    private lateinit var textViewSave: TextView
    private lateinit var recycleView: RecyclerView

    private lateinit var ringtoneName: String
    private lateinit var ringtonePath: String
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_ringtone)

        initialComponent()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_close_activity -> {
                this.finish()
            }
            R.id.textView_save -> {
                val intent = Intent().apply {
                    putExtra("ringtoneName", ringtoneName)
                    putExtra("ringtonePath", ringtonePath)
                }
                setResult(4, intent)
                this.finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun initialComponent() {
        buttonCloseActivity = findViewById(R.id.button_close_activity)
        textViewSave = findViewById(R.id.textView_save)
        recycleView = findViewById(R.id.ringtone_recycler)

        buttonCloseActivity.setOnClickListener(this)
        textViewSave.setOnClickListener(this)

        recycleView.layoutManager = LinearLayoutManager(this)
        val adapter = SetRingtoneAdapter(this)
        adapter.setOnItemClickListener(object : OnRecyclerViewItemClickListener {
            override fun onItemClick(view: View, data: String, position: Int) {
                ringtoneName = data
                val ringtoneManager = RingtoneManager(this@SetRingtoneActivity)
                ringtoneManager.setType(RingtoneManager.TYPE_RINGTONE)
                ringtoneManager.cursor
                val ringtoneUri = ringtoneManager.getRingtoneUri(position)
                ringtonePath = ringtoneUri.toString()
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                } else {
                    if (mediaPlayer!!.isPlaying) {
                        mediaPlayer!!.stop()
                    }
                    mediaPlayer!!.reset()
                }
                mediaPlayer!!.setDataSource(this@SetRingtoneActivity, ringtoneUri)
                mediaPlayer!!.setVolume(1f, 1f)
                mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                mediaPlayer!!.isLooping = false
                mediaPlayer!!.prepare()
                mediaPlayer!!.start()
            }
        })
        recycleView.adapter = adapter
    }

}
