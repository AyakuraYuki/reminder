package cc.ayakurayuki.reminder.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

/**
 * Created by ayakurayuki on 2017/12/28.
 */
class PermissionUtils {

    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun permissionSet(vararg permissions: String): Boolean {
        return permissions.any { isLackPermission(it) }
    }

    fun isLackPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED
    }

}