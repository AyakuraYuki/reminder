package cc.ayakurayuki.reminder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.util.Log
import cc.ayakurayuki.reminder.database.DBSupport

class AddAlarmActivity : AppCompatActivity() {

    companion object {
        private val tag: String = AddAlarmActivity::class.java.name
    }

    private lateinit var buttonCloseActivity: AppCompatImageButton

    private lateinit var dbSupport: DBSupport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        buttonCloseActivity = findViewById(R.id.button_close_activity)
        buttonCloseActivity.setOnClickListener {
            this.finish()
        }

        dbSupport = DBSupport(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "on destroy")
    }

}
