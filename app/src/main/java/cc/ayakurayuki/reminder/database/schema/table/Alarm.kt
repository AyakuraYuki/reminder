package cc.ayakurayuki.reminder.database.schema.table

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class Alarm {

    companion object {
        const val tableName: String = "alarmList"
    }

    class Column {
        companion object {
            const val id = "id"
            const val title = "title"
            const val allDay = "allDay"
            const val vibrate = "vibrate"
            const val year = "year"
            const val month = "month"
            const val day = "day"
            const val startTimeHour = "startTimeHour"
            const val startTimeMinute = "startTimeMinute"
            const val endTimeHour = "endTimeHour"
            const val endTimeMinute = "endTimeMinute"
            const val alarmTime = "alarmTime"
            const val alarmColor = "alarmColor"
            const val alarmTonePath = "alarmTonePath"
            const val local = "local"
            const val description = "description"
            const val replay = "replay"
        }
    }

}