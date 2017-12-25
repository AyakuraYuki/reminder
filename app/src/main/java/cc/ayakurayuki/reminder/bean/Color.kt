package cc.ayakurayuki.reminder.bean

/**
 * Created by ayakurayuki on 2017/12/25.
 */

class Color(var name: String, var code: Int) {
    companion object {
        val ALARM_DEFAULT: String = "默认颜色"
        val BASIL_GREEN: String = "罗勒绿"
        val SHINY_YELLOW: String = "耀眼黄"
        val TOMATO_RED: String = "番茄红"
        val UNDERTONE_GREY: String = "低调灰"
        val ORANGE: String = "橘红"
        val DEEP_SKY_BLUE: String = "深空蓝"
    }
}
