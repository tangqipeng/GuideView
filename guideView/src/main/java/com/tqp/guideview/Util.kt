package com.tqp.guideview

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import java.lang.reflect.Method
import kotlin.math.roundToInt

/**
 * @author  tangqipeng
 * @date  4/21/21 11:19 AM
 * @email tangqipeng@aograph.com
 */

fun logd(tag: String, msg: String) {
    Log.d(tag, msg)
}

fun loge(tag: String, msg: String) {
    Log.e(tag, msg)
}

private val DENSITY: Float = Resources.getSystem().displayMetrics.density

fun dp2px(dp: Int): Int{
    return (dp * DENSITY).roundToInt()
}

fun getDisplayHeight(): Int{
    val displayMetrics = Resources.getSystem().displayMetrics
    return displayMetrics.heightPixels
}

fun getDecorViewHeight(activity: Activity): Int{
    return activity.window.decorView.height
}

fun getUsableDisplayHeight(context: Context): Int{
    return getHeight(context) - getStatusBarHeight(context)
}

fun getDisplayWidth(): Int{
    val displayMetrics = Resources.getSystem().displayMetrics
    return displayMetrics.widthPixels
}

fun getHeight(context: Context): Int {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val dm = DisplayMetrics()
    display.getMetrics(dm)
    return dm.heightPixels
}

fun getStatusBarHeight(context: Context): Int {
    return getSizeByReflection(context, "status_bar_height")
}

private fun getSizeByReflection(context: Context, field: String?): Int {
    var size = -1
    try {
        val clazz = Class.forName("com.android.internal.R\$dimen")
        val `object` = clazz.newInstance()
        val height = clazz.getField(field)[`object`].toString().toInt()
        size = context.resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return size
}

fun getToolBarHeight(activity: Activity): Int {
    val tv = TypedValue()
    if (activity.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize (tv.data, activity.resources.displayMetrics)
    }
    return 0
}

fun getVirtualBarHeight(context: Context): Int {
    var vh = 0
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = windowManager.defaultDisplay
    val dm = DisplayMetrics()
    try {
        val c = Class.forName("android.view.Display")
        val method: Method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
        method.invoke(display, dm)
        vh = dm.heightPixels - display.height
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return vh
}