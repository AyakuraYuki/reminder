package cc.ayakurayuki.reminder.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import cc.ayakurayuki.reminder.MainActivity
import cc.ayakurayuki.reminder.R
import cc.ayakurayuki.reminder.bean.AlarmBean
import cc.ayakurayuki.reminder.database.DBSupport

class AlarmDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dbSupport: DBSupport
    private lateinit var bean: AlarmBean

    private lateinit var buttonCloseActivity: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var updateFab: FloatingActionButton

    private lateinit var detailAlarmTitle: TextView
    private lateinit var detailAlarmDate: TextView
    private lateinit var detailAlarmStartEndTime: TextView
    private lateinit var detailAlarmRemind: TextView
    private lateinit var detailAlarmLocal: TextView
    private lateinit var detailAlarmDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_detail)
        dbSupport = DBSupport(applicationContext)
        val id = intent.extras.get("id").toString()
        bean = dbSupport.get(id.toInt())
        initialComponents()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_close_activity -> {
                this@AlarmDetailActivity.finish()
            }
            R.id.button_delete -> {
                val builder = AlertDialog.Builder(this).apply {
                    setIcon(R.drawable.ic_launcher_foreground)
                    title = "确认"
                    setMessage("是否要删除这个活动?")
                    setNegativeButton("不删除", null)
                    setPositiveButton("删除", { _, _ ->
                        dbSupport.delete(bean.id)
                        val intent = Intent(this@AlarmDetailActivity, MainActivity::class.java)
                        startActivity(intent)
                        this@AlarmDetailActivity.finish()
                    })
                }
                val dialog = builder.create()
                dialog.show()
            }
            R.id.update_fab -> {
                val intent = Intent(this@AlarmDetailActivity, AddAlarmActivity::class.java).apply {
                    putExtra("id", bean.id)
                }
                startActivity(intent)
                this@AlarmDetailActivity.finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        dbSupport.close()
    }

    @SuppressLint("SetTextI18n")
    private fun initialComponents() {
        buttonCloseActivity = findViewById(R.id.button_close_activity)
        buttonDelete = findViewById(R.id.button_delete)
        updateFab = findViewById(R.id.update_fab)
        detailAlarmTitle = findViewById(R.id.detail_alarm_title)
        detailAlarmDate = findViewById(R.id.detail_alarm_date)
        detailAlarmStartEndTime = findViewById(R.id.detail_alarm_start_end_time)
        detailAlarmRemind = findViewById(R.id.detail_alarm_remind)
        detailAlarmLocal = findViewById(R.id.detail_alarm_local)
        detailAlarmDescription = findViewById(R.id.detail_alarm_description)

        buttonCloseActivity.setOnClickListener(this)
        buttonDelete.setOnClickListener(this)
        updateFab.setOnClickListener(this)

        detailAlarmTitle.text = bean.title
        detailAlarmDate.text = "${bean.year}-${bean.month}-${bean.day}"
        detailAlarmStartEndTime.text = "${bean.startTimeHour}:${bean.startTimeMinute} ~ ${bean.endTimeHour}:${bean.endTimeMinute}"
        detailAlarmRemind.text = "${bean.alarmTime}"
        detailAlarmLocal.text = "${bean.local}"
        detailAlarmDescription.text = "${bean.description}"
    }

}
