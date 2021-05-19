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
        val stateOb = clazz.newInstance()
        val height = clazz.getField(field)[stateOb].toString().toInt()
        size = context.resources.getDimensionPixelSize(height)
    } catch (e: Exception) {
        if (size == -1){
            val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                size = context.resources.getDimensionPixelSize(resourceId)
            }
        }
    }
    return size
}

fun getToolBarHeight(context: Context): Int {
    val tv = TypedValue()
    if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize (tv.data, context.resources.displayMetrics)
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