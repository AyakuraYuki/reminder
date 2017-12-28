package cc.ayakurayuki.reminder.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import cc.ayakurayuki.reminder.R
import cc.ayakurayuki.reminder.database.DBSupport

class AlarmDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var dbSupport: DBSupport

    private lateinit var buttonCloseActivity: ImageButton
    private lateinit var buttonDelete: ImageButton
    private lateinit var updateFab: FloatingActionButton

    private lateinit var detailAlarmDate: TextView
    private lateinit var detailAlarmStartEndTime: TextView
    private lateinit var detailAlarmRemind: TextView
    private lateinit var detailAlarmLocal: TextView
    private lateinit var detailAlarmDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_detail)
        initialComponents()
        dbSupport = DBSupport(applicationContext)
        val id = intent.extras.get("")
//        val bean = dbSupport.get()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button_close_activity -> {

            }
            R.id.button_delete -> {

            }
            R.id.update_fab -> {

            }
        }
    }

    private fun initialComponents() {
        buttonCloseActivity = findViewById(R.id.button_close_activity)
        buttonDelete = findViewById(R.id.button_delete)
        updateFab = findViewById(R.id.update_fab)
        detailAlarmDate = findViewById(R.id.detail_alarm_date)
        detailAlarmStartEndTime = findViewById(R.id.detail_alarm_start_end_time)
        detailAlarmRemind = findViewById(R.id.detail_alarm_remind)
        detailAlarmLocal = findViewById(R.id.detail_alarm_local)
        detailAlarmDescription = findViewById(R.id.detail_alarm_description)

        buttonCloseActivity.setOnClickListener(this)
        buttonDelete.setOnClickListener(this)
        updateFab.setOnClickListener(this)
    }

}
