package cc.ayakurayuki.reminder.util

import cc.ayakurayuki.reminder.R
import cc.ayakurayuki.reminder.bean.Color

/**
 * Created by ayakurayuki on 2017/12/25.
 */
class ColorUtils {

    companion object {
        val ALARM_DEFAULT: Color = Color(Color.ALARM_DEFAULT, R.color.alarm_default)
        val BASIL_GREEN: Color = Color(Color.BASIL_GREEN, R.color.basil_green)
        val SHINY_YELLOW: Color = Color(Color.SHINY_YELLOW, R.color.shiny_yellow)
        val TOMATO_RED: Color = Color(Color.TOMATO_RED, R.color.tomato_red)
        val UNDERTONE_GREY: Color = Color(Color.UNDERTONE_GREY, R.color.undertone_grey)
        val ORANGE: Color = Color(Color.ORANGE, R.color.orange)
        val DEEP_SKY_BLUE: Color = Color(Color.DEEP_SKY_BLUE, R.color.deep_sky_blue)

        fun colorFromString(string: String?): Int {
            return when (string) {
                ALARM_DEFAULT.name -> ALARM_DEFAULT.code
                BASIL_GREEN.name -> BASIL_GREEN.code
                SHINY_YELLOW.name -> SHINY_YELLOW.code
                TOMATO_RED.name -> TOMATO_RED.code
                UNDERTONE_GREY.name -> UNDERTONE_GREY.code
                ORANGE.name -> ORANGE.code
                DEEP_SKY_BLUE.name -> DEEP_SKY_BLUE.code
                else -> 0
            }
        }
    }

}