package cc.ayakurayuki.reminder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.util.Log
import android.view.View
import android.widget.*
import cc.ayakurayuki.reminder.bean.ReplayTimeEnum
import cc.ayakurayuki.reminder.database.DBSupport

class AddAlarmActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val tag: String = AddAlarmActivity::class.java.name
    }

    // region 动作栏
    private lateinit var buttonCloseActivity: AppCompatImageButton
    private lateinit var textViewSave: TextView
    // endregion
    // region 表单组件
    private lateinit var eventTitle: EditText
    private lateinit var switchAllDay: Switch
    private lateinit var eventDate: TextView
    private lateinit var eventStartTime: TextView
    private lateinit var eventEndTime: TextView
    private lateinit var replayType: Spinner
    // endregion

    private lateinit var dbSupport: DBSupport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        initialComponents()
        dbSupport = DBSupport(applicationContext)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_close_activity -> {
                this.finish()
            }
            R.id.textView_save -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "on destroy")
    }

    /**
     * 初始化组件
     */
    private fun initialComponents() {
        buttonCloseActivity = findViewById(R.id.button_close_activity)
        textViewSave = findViewById(R.id.textView_save)
        eventTitle = findViewById(R.id.event_title)
        switchAllDay = findViewById(R.id.switch_all_day)
        eventDate = findViewById(R.id.event_date)
        eventStartTime = findViewById(R.id.event_start_time)
        eventEndTime = findViewById(R.id.event_end_time)
        replayType = findViewById(R.id.spinner_replay)

        buttonCloseActivity.setOnClickListener(this)
        textViewSave.setOnClickListener(this)
        eventDate.setOnClickListener(this)
        eventStartTime.setOnClickListener(this)
        eventEndTime.setOnClickListener(this)
        bindSpinner()
    }

    private fun bindSpinner() {
        val data = arrayListOf(
                ReplayTimeEnum.NONE.title,
                ReplayTimeEnum.DAILY.title,
                ReplayTimeEnum.WEEKLY.title,
                ReplayTimeEnum.YEARLY.title
        )
        replayType.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

}
