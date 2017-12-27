package cc.ayakurayuki.reminder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cc.ayakurayuki.reminder.database.DBSupport

class AddAlarmActivity : AppCompatActivity() {

    companion object {
        private val tag: String = AddAlarmActivity::class.java.name
    }

    private lateinit var dbSupport: DBSupport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        dbSupport = DBSupport(applicationContext)
    }

}
