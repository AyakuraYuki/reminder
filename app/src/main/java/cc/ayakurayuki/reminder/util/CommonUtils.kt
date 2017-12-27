package cc.ayakurayuki.reminder.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
 * Created by ayakurayuki on 2017/12/27.
 */
object CommonUtils {

    private var toast: Toast? = null

    fun showTextToast(applicationContext: Context, message: String) {
        if (toast == null) {
            toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(message)
        }
        toast!!.setGravity(Gravity.BOTTOM, 0, 50)
        toast!!.show()
    }

}