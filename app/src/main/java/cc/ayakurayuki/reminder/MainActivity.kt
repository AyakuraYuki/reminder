package cc.ayakurayuki.reminder

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import cc.ayakurayuki.reminder.activity.AddAlarmActivity
import cc.ayakurayuki.reminder.database.DBSupport
import cc.ayakurayuki.reminder.service.AlarmServiceBroadcastReceiver
import cc.ayakurayuki.reminder.util.ColorUtils
import cc.ayakurayuki.reminder.util.CommonUtils
import cc.ayakurayuki.reminder.util.PermissionUtils
import cc.ayakurayuki.reminder.util.PrefUtils
import com.github.tibolte.agendacalendarview.AgendaCalendarView
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent
import com.github.tibolte.agendacalendarview.models.CalendarEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        private val tag: String = MainActivity::class.java.name
        private val requestCodeSign: Int = 1
        val permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
        const val extraName = "alarm"
    }

    private lateinit var mAgendaCalendarView: AgendaCalendarView
    private lateinit var dbSupport: DBSupport
    private lateinit var permissionValidator: PermissionUtils
    private var isAllowAlert = false

    /**
     * 初始化
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            Log.d(tag, "fab clicked")
            val intent = Intent(this@MainActivity, AddAlarmActivity::class.java).apply {
                putExtra("id", 0)
            }
            startActivity(intent)
            this.finish()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        dbSupport = DBSupport(applicationContext)
        initialAgendaCalendar()

        permissionValidator = PermissionUtils(this)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isAllowAlert = PrefUtils.getBoolean(this, "isAllowAlert", false)
            if (!isAllowAlert) {
                showPermissionDialog()
            }
        } else {
            val alarmServiceIntent = Intent(this, AlarmServiceBroadcastReceiver::class.java)
            this.sendBroadcast(alarmServiceIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 权限申请
        permissions.filter { permissionValidator.permissionSet(it) }
                .forEach { requestPermissions(permissions, 0) }
    }

    /**
     * 返回键转入后台
     */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 创建菜单
     * @param menu 菜单项
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * 菜单选项按下的事件
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * 抽屉菜单事件绑定
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_send -> {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:AyakuraYuki@users.noreply.github.com")
                    putExtra(Intent.EXTRA_SUBJECT, "问题反馈")
                }
                startActivity(intent)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * 程序关闭事件，同时需要关闭数据库连接
     */
    override fun onDestroy() {
        super.onDestroy()
        dbSupport.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            requestCodeSign -> {
                if (Settings.canDrawOverlays(this)) {
                    CommonUtils.showTextToast(this, "弹窗权限开启")
                    PrefUtils.setBoolean(this@MainActivity, "isAllowAlert", true)
                } else {
                    PrefUtils.setBoolean(this@MainActivity, "isAllowAlert", false)
                }
            }
        }
    }

    /**
     * 初始化日历视图
     */
    private fun initialAgendaCalendar() {
        mAgendaCalendarView = findViewById(R.id.agenda_calendar_view)

        // 设定日历的最大允许日期和最小限制日期
        // 最早不超过两个月，最晚不超过一年
        val minDate = Calendar.getInstance()
        val maxDate = Calendar.getInstance()

        minDate.add(Calendar.MONTH, -2)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)

        val eventList = ArrayList<CalendarEvent>()
        mockList(eventList)

        mAgendaCalendarView.init(
                eventList, minDate, maxDate, Locale.getDefault(), CalendarDatePickController(toolbar, this)
        )
    }

    private fun mockList(eventList: MutableList<CalendarEvent>) {
        val all = dbSupport.all()
        all.forEach {
            val startTime = Calendar.getInstance().apply {
                set(Calendar.YEAR, it.year)
                set(Calendar.MONTH, it.month)
                set(Calendar.DAY_OF_MONTH, it.day)
                set(Calendar.HOUR_OF_DAY, it.startTimeHour)
                set(Calendar.MINUTE, it.startTimeMinute)
            }
            val endTime = Calendar.getInstance().apply {
                set(Calendar.YEAR, it.year)
                set(Calendar.MONTH, it.month)
                set(Calendar.DAY_OF_MONTH, it.day)
                set(Calendar.HOUR_OF_DAY, it.endTimeHour)
                set(Calendar.MINUTE, it.endTimeMinute)
            }
            val event = BaseCalendarEvent(
                    it.title,
                    it.description,
                    it.local,
                    ContextCompat.getColor(this, ColorUtils.colorFromString(it.alarmColor)),
                    startTime,
                    endTime,
                    it.isAllDay()
            ).apply {
                id = it.id.toLong()
            }
            eventList.add(event)
        }
    }

    private fun showPermissionDialog() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("弹窗需要开启权限")
                .setPositiveButton("开启") { _, _ -> requestAlertWindowPermission() }
                .setNegativeButton("取消") { _, _ ->
                    CommonUtils.showTextToast(this, "没有做任何操作")
                }
        builder.create().show()
    }

    private fun requestAlertWindowPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + packageName)
        startActivityForResult(intent, requestCodeSign)
    }

}
