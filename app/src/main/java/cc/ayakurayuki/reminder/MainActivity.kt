package cc.ayakurayuki.reminder

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cc.ayakurayuki.reminder.database.DBSupport
import cc.ayakurayuki.reminder.util.ColorUtils
import com.github.tibolte.agendacalendarview.AgendaCalendarView
import com.github.tibolte.agendacalendarview.models.BaseCalendarEvent
import com.github.tibolte.agendacalendarview.models.CalendarEvent
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAgendaCalendarView: AgendaCalendarView
    private lateinit var dbSupport: DBSupport

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        dbSupport = DBSupport(applicationContext)
        initialAgendaCalendar()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        dbSupport.close()
    }

    private fun initialAgendaCalendar() {
        mAgendaCalendarView = findViewById(R.id.agenda_calendar_view)

        // minimum and maximum date of our calendar
        // 2 month behind, one year ahead, example: March 2015 <-> May 2015 <-> May 2016
        val minDate = Calendar.getInstance()
        val maxDate = Calendar.getInstance()

        minDate.add(Calendar.MONTH, -2)
        minDate.set(Calendar.DAY_OF_MONTH, 1)
        maxDate.add(Calendar.YEAR, 1)

        val eventList = ArrayList<CalendarEvent>()
        mockList(eventList)

        mAgendaCalendarView.init(
                eventList, minDate, maxDate, Locale.getDefault(), CalendarDatePickController(toolbar)
        )
    }

    private fun mockList(eventList: MutableList<CalendarEvent>) {
        val all = dbSupport.all()
        all.forEach {
            val startTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, it.startTimeHour)
                set(Calendar.MINUTE, it.startTimeMinute)
            }
            val endTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, it.endTimeHour)
                set(Calendar.MINUTE, it.endTimeMinute)
            }
            val event = BaseCalendarEvent(
                    it.title,
                    it.description,
                    it.local,
                    ColorUtils.colorFromString(it.alarmColor),
                    startTime,
                    endTime,
                    it.isAllDay()
            )
            eventList.add(event)
        }
    }

    /*
    private fun getToolbarTitleView(activity: AppCompatActivity, toolbar: Toolbar): TextView? {
        val actionBar = activity.supportActionBar
        var actionbarTitle: CharSequence? = null
        if (actionBar != null) {
            actionbarTitle = actionBar.title
        }
        actionbarTitle = if (TextUtils.isEmpty(actionbarTitle)) toolbar.title else actionbarTitle
        if (TextUtils.isEmpty(actionbarTitle)) {
            return null
        }
        // can't find if title not set
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view != null && view is TextView) {
                val title = view.text
                if (!TextUtils.isEmpty(title) && actionbarTitle == title && view.id == View.NO_ID) {
                    //Toolbar does not assign id to views with layout params SYSTEM, hence getId() == View.NO_ID
                    //in same manner subtitle TextView can be obtained.
                    return view
                }
            }
        }
        return null
    }
    */

}
