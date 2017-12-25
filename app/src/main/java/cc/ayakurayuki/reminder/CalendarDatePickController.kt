package cc.ayakurayuki.reminder

import android.annotation.SuppressLint
import android.support.v7.widget.Toolbar
import android.widget.TextView
import com.github.tibolte.agendacalendarview.CalendarPickerController
import com.github.tibolte.agendacalendarview.models.CalendarEvent
import com.github.tibolte.agendacalendarview.models.DayItem
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class CalendarDatePickController(private var toolbar: Toolbar) : CalendarPickerController {

    override fun onDaySelected(dayItem: DayItem?) {
        val toolbarTextView = getToolbarTitleView(toolbar)
        @SuppressLint("SimpleDateFormat")
        toolbarTextView?.text = SimpleDateFormat("yyyy年MM月dd日").format(dayItem?.date)
    }

    override fun onScrollToDate(calendar: Calendar?) {
        val toolbarTextView = getToolbarTitleView(toolbar)
        @SuppressLint("SimpleDateFormat")
        toolbarTextView?.text = SimpleDateFormat("yyyy年MM月dd日").format(calendar?.time)
    }

    override fun onEventSelected(event: CalendarEvent?) {

    }

    private fun getToolbarTitleView(toolbar: Toolbar): TextView? {
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view != null && view is TextView) {
                return view
            }
        }
        return null
    }

}