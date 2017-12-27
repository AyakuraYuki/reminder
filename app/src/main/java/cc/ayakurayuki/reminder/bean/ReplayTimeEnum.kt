package cc.ayakurayuki.reminder.bean

/**
 * Created by ayakurayuki on 2017/12/25.
 */
enum class ReplayTimeEnum(var title: String, var time: Long) {

    NONE("不重复", 0),
    DAILY("每天", 86400000),
    WEEKLY("每周", 604800000),
    YEARLY("每年", 31536000000L)

}
