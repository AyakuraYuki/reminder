package cc.ayakurayuki.reminder.bean

import java.util.*

/**
 * Created by ayakurayuki on 2017/12/25.
 */
enum class AlarmTimeEnum(var title: String, var calendarType: Int, var time: Int) {

    NONE("无", -1, 0),
    TEN_MINUTE("提前十分钟", Calendar.MINUTE, -10),
    HOUR("提前一个小时", Calendar.HOUR_OF_DAY, -1),
    DAY("提前一天", Calendar.DAY_OF_MONTH, -1)

}
