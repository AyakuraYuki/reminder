package cc.ayakurayuki.reminder.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by ayakurayuki on 2017/12/28.
 */
class PrefUtils {

    companion object {
        private val PREF_NAME = "config"

        fun getBoolean(ctx: Context, key: String, defaultValue: Boolean?): Boolean {
            val sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) as SharedPreferences
            return sp.getBoolean(key, defaultValue!!)
        }

        @SuppressLint("ApplySharedPref")
        fun setBoolean(ctx: Context, key: String, value: Boolean?) {
            val sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) as SharedPreferences
            sp.edit().putBoolean(key, value!!).commit()
        }

        fun getString(ctx: Context, key: String, defaultValue: String): String? {
            val sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) as SharedPreferences
            return sp.getString(key, defaultValue)
        }

        @SuppressLint("ApplySharedPref")
        fun setString(ctx: Context, key: String, value: String) {
            val sp = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) as SharedPreferences
            sp.edit().putString(key, value).commit()
        }
    }

}
