package cc.ayakurayuki.reminder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.util.Log
import android.view.View
import android.widget.*
import cc.ayakurayuki.reminder.bean.AlarmTimeEnum
import cc.ayakurayuki.reminder.bean.Color
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
    private lateinit var notificationType: Spinner
    private lateinit var textViewRingtone: TextView
    private lateinit var switchVibration: Switch
    private lateinit var locationEditView: EditText
    private lateinit var color: Spinner
    private lateinit var remark: EditText
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
            R.id.event_date -> {

            }
            R.id.event_start_time -> {

            }
            R.id.event_end_time -> {

            }
            R.id.textView_select_ringtone -> {

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
        // region find view by id
        buttonCloseActivity = findViewById(R.id.button_close_activity)
        textViewSave = findViewById(R.id.textView_save)
        eventTitle = findViewById(R.id.event_title)
        switchAllDay = findViewById(R.id.switch_all_day)
        eventDate = findViewById(R.id.event_date)
        eventStartTime = findViewById(R.id.event_start_time)
        eventEndTime = findViewById(R.id.event_end_time)
        replayType = findViewById(R.id.spinner_replay)
        notificationType = findViewById(R.id.spinner_notifications)
        textViewRingtone = findViewById(R.id.textView_select_ringtone)
        switchVibration = findViewById(R.id.switch_vibrate)
        locationEditView = findViewById(R.id.event_location)
        color = findViewById(R.id.spinner_color_lens)
        remark = findViewById(R.id.remark)
        // endregion
        // region bind on click listener
        buttonCloseActivity.setOnClickListener(this)
        textViewSave.setOnClickListener(this)
        eventDate.setOnClickListener(this)
        eventStartTime.setOnClickListener(this)
        eventEndTime.setOnClickListener(this)
        bindSpinner(replayType)
        bindSpinner(notificationType)
        textViewRingtone.setOnClickListener(this)
        bindSpinner(color)
        // endregion
    }

    // 初始化Spinner下拉选项
    private fun bindSpinner(spinner: Spinner) {
        fun bind(data: ArrayList<String>) {
            spinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinner.setSelection(0)
        }
        when (spinner.id) {
            R.id.spinner_replay -> {
                val data = arrayListOf(
                        ReplayTimeEnum.NONE.title,
                        ReplayTimeEnum.DAILY.title,
                        ReplayTimeEnum.WEEKLY.title,
                        ReplayTimeEnum.YEARLY.title
                )
                bind(data)
            }
            R.id.spinner_notifications -> {
                val data = arrayListOf(
                        AlarmTimeEnum.NONE.title,
                        AlarmTimeEnum.TEN_MINUTE.title,
                        AlarmTimeEnum.HOUR.title,
                        AlarmTimeEnum.DAY.title
                )
                bind(data)
            }
            R.id.spinner_color_lens -> {
                val data = arrayListOf(
                        Color.ALARM_DEFAULT,
                        Color.BASIL_GREEN,
                        Color.SHINY_YELLOW,
                        Color.TOMATO_RED,
                        Color.UNDERTONE_GREY,
                        Color.ORANGE,
                        Color.DEEP_SKY_BLUE
                )
                bind(data)
            }
        }
    }

}
