package cc.ayakurayuki.reminder.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatImageButton
import android.util.Log
import android.view.View
import android.widget.*
import cc.ayakurayuki.reminder.MainActivity
import cc.ayakurayuki.reminder.R
import cc.ayakurayuki.reminder.bean.AlarmBean
import cc.ayakurayuki.reminder.bean.AlarmTimeEnum
import cc.ayakurayuki.reminder.bean.Color
import cc.ayakurayuki.reminder.bean.ReplayTimeEnum
import cc.ayakurayuki.reminder.database.DBSupport
import cc.ayakurayuki.reminder.service.SendAlarmBroadcast
import cc.ayakurayuki.reminder.util.ColorUtils
import cc.ayakurayuki.reminder.util.CommonUtils
import kotlinx.android.synthetic.main.activity_add_alarm.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddAlarmActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val tag: String = AddAlarmActivity::class.java.name
        private val ringtoneRequestCode: Int = 1
        val colorData = arrayListOf(
                Color.ALARM_DEFAULT,
                Color.BASIL_GREEN,
                Color.SHINY_YELLOW,
                Color.TOMATO_RED,
                Color.UNDERTONE_GREY,
                Color.ORANGE,
                Color.DEEP_SKY_BLUE
        )
        val replayData = arrayListOf(
                ReplayTimeEnum.NONE.title,
                ReplayTimeEnum.DAILY.title,
                ReplayTimeEnum.WEEKLY.title,
                ReplayTimeEnum.YEARLY.title
        )
        val alarmTypeData = arrayListOf(
                AlarmTimeEnum.NONE.title,
                AlarmTimeEnum.TEN_MINUTE.title,
                AlarmTimeEnum.HOUR.title,
                AlarmTimeEnum.DAY.title
        )
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

    private lateinit var vibrator: Vibrator
    private lateinit var dataPicker: DatePickerDialog
    private lateinit var timePicker: TimePickerDialog
    private lateinit var dbSupport: DBSupport
    private lateinit var alarmBean: AlarmBean

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)
        initialDBAndBean()
        initialComponents()
        prepareData()
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_close_activity -> {
                startActivity(Intent(this@AddAlarmActivity, MainActivity::class.java))
                this.finish()
            }
            R.id.textView_save -> {
                when {
                    eventDate.text.toString() == getString(R.string.select_event_date_tip) -> {
                        CommonUtils.showTextToast(this, "活动日期未选择")
                    }
                    !switchAllDay.isChecked -> {
                        when {
                            eventStartTime.text.toString() == getString(R.string.select_start_time_tip) -> {
                                CommonUtils.showTextToast(this, "开始时间未选择")
                            }
                            eventEndTime.text.toString() == getString(R.string.select_end_time_tip) -> {
                                CommonUtils.showTextToast(this, "结束时间未选择")
                            }
                            else -> {
                                save()
                                startActivity(Intent(this@AddAlarmActivity, MainActivity::class.java))
                                this.finish()
                            }
                        }
                    }
                    else -> {
                        save()
                        startActivity(Intent(this@AddAlarmActivity, MainActivity::class.java))
                        this.finish()
                    }
                }
            }
            R.id.event_date -> {
                getDatePickerDialog()
                dataPicker.show()
            }
            R.id.event_start_time -> {
                getTimePickerDialog(eventStartTime)
                timePicker.show()
            }
            R.id.event_end_time -> {
                getTimePickerDialog(eventEndTime)
                timePicker.show()
            }
            R.id.textView_select_ringtone -> {
                startActivityForResult(Intent(this@AddAlarmActivity, SetRingtoneActivity::class.java), ringtoneRequestCode)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            ringtoneRequestCode -> {
                val name = data?.getStringExtra("ringtoneName")
                val path = data?.getStringExtra("ringtonePath")
                textViewRingtone.text = if (name == "") "选择铃声" else name
                alarmBean.alarmTonePath = if (path == "") "" else path
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AddAlarmActivity, MainActivity::class.java))
        this.finish()
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
        bindColorSpinner(color)
        // endregion
        switchVibration.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    vibrator.vibrate(500)
                }
            }
        }
    }

    private fun initialDBAndBean() {
        dbSupport = DBSupport(applicationContext)
        val id = intent.extras.get("id").toString().toInt()
        alarmBean = if (id == 0) AlarmBean() else dbSupport.get(id)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun prepareData() {
        if (alarmBean.id != 0) {
            eventTitle.setText(alarmBean.title)
            switchAllDay.isChecked = alarmBean.isAllDay()
            eventDate.text = "活动日期: ${SimpleDateFormat("yyyy年MM月dd日  EE").format(Calendar.getInstance().apply {
                set(alarmBean.year, alarmBean.month, alarmBean.day)
            }.time)}"
            eventStartTime.text = "开始时间: ${SimpleDateFormat("HH:mm").format(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarmBean.startTimeHour)
                set(Calendar.MINUTE, alarmBean.startTimeMinute)
            }.time)}"
            eventEndTime.text = "结束时间: ${SimpleDateFormat("HH:mm").format(Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarmBean.endTimeHour)
                set(Calendar.MINUTE, alarmBean.endTimeMinute)
            }.time)}"
            replayType.setSelection(replayData.indexOf(alarmBean.replay))
            notificationType.setSelection(alarmTypeData.indexOf(alarmBean.alarmTime))
            textViewRingtone.text = "请重新选择铃声"
            switchVibration.isChecked = alarmBean.isVibrate()
            locationEditView.setText(alarmBean.local)
            color.setSelection(colorData.indexOf(alarmBean.alarmColor))
            remark.setText(alarmBean.description)
        }
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
                bind(replayData)
            }
            R.id.spinner_notifications -> {
                bind(alarmTypeData)
            }
        }
    }

    private fun bindColorSpinner(colorSpinner: Spinner) {
        val list = ArrayList<Map<String, Any>>()
        colorData.forEach {
            val map = HashMap<String, Any>().apply {
                put("iv_color_circle", ColorUtils.colorFromString(it))
                put("color_title", it)
            }
            list.add(map)
        }
        colorSpinner.adapter = SimpleAdapter(
                this,
                list,
                R.layout.color_spinner_dropdown_item,
                arrayOf("iv_color_circle", "color_title"),
                intArrayOf(R.id.iv_color_circle, R.id.color_title)
        )
        color.setSelection(0)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getDatePickerDialog() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        dataPicker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    val cal = Calendar.getInstance().apply {
                        // 将选取事件获得的参数转入监听器事件中
                        set(year, monthOfYear, dayOfMonth)
                    }
                    eventDate.text = "活动日期: ${SimpleDateFormat("yyyy年MM月dd日  EE").format(cal.time)}" // 显示到界面上
                    alarmBean.year = year // 设置年
                    alarmBean.month = monthOfYear // 设置月
                    alarmBean.day = dayOfMonth // 设置日
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun getTimePickerDialog(view: View) {
        val calendar = Calendar.getInstance()
        timePicker = TimePickerDialog(
                this,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val cal = Calendar.getInstance().apply {
                        // 将选取事件获得的参数转入监听器事件中
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }
                    // 根据传入参数判断对象
                    when (view.id) {
                        R.id.event_start_time -> {
                            eventStartTime.text = "开始时间: ${SimpleDateFormat("HH:mm").format(cal.time)}"
                            alarmBean.startTimeHour = hourOfDay
                            alarmBean.startTimeMinute = minute
                        }
                        R.id.event_end_time -> {
                            eventEndTime.text = "结束时间: ${SimpleDateFormat("HH:mm").format(cal.time)}"
                            alarmBean.endTimeHour = hourOfDay
                            alarmBean.endTimeMinute = minute
                        }
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        )
    }

    private fun save() {
        alarmBean.title = eventTitle.text.toString()
        if (switchAllDay.isChecked) {
            alarmBean.setAllDay()
        } else {
            alarmBean.unsetAllDay()
        }
        if (switchVibration.isChecked) {
            alarmBean.setVibrate()
        } else {
            alarmBean.unsetVibrate()
        }
        alarmBean.alarmTime = spinner_notifications.selectedItem.toString()
        alarmBean.alarmColor = colorData[spinner_color_lens.selectedItemPosition]
        alarmBean.local = locationEditView.text.toString()
        alarmBean.description = remark.text.toString()
        alarmBean.replay = spinner_replay.selectedItem.toString()
        alarmBean.month += 1
        dbSupport.save(alarmBean)
        SendAlarmBroadcast.startAlarmService(this)
    }

}
