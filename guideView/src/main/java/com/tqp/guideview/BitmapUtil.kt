package com.tqp.guideview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes


/**
 * @author  tangqipeng
 * @date  3/26/21 10:28 AM
 * @email tangqipeng@aograph.com
 */
object BitmapUtil {

    private val sCanvas = Canvas()

    /**
     *
     * <获取view的bitmap形式>
     * @param view
     * @return
     * @see [类、类.方法、类.成员]
    </获取view的bitmap形式> */
    fun createBitmapFromView(view: View): Bitmap {
        //是ImageView直接获取
        if (view is ImageView) {
            val drawable = view.drawable
            if (drawable != null && drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        view.clearFocus()
        val bitmap: Bitmap = createBitmapSafely(view.width, view.height, Bitmap.Config.ARGB_8888, 1)!!
        synchronized(sCanvas) {
            val canvas: Canvas = sCanvas
            canvas.setBitmap(bitmap)
            view.draw(canvas)
            canvas.setBitmap(null)
        }
        return bitmap
    }

    //view 转bitmap
    fun convertViewToBitmap(view: View): Bitmap? {
        view.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        return view.drawingCache
    }

    fun getRotateBitmapFromDrawable(context: Context, @DrawableRes id: Int, degrees: Float) : Bitmap{
        val matrix = Matrix()
        var bitmap = BitmapFactory.decodeResource(context.resources, id)
        // 设置旋转角度
        // 设置旋转角度
        matrix.setRotate(degrees)
        // 重新绘制Bitmap
        // 重新绘制Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return bitmap
    }

    fun getRotateBitmapFromDrawable1(context: Context, @DrawableRes id: Int, degrees: Float) : Bitmap{
        val bmpOriginal = BitmapFactory.decodeResource(context.resources, id)
        val bmResult =
            Bitmap.createBitmap(bmpOriginal.width, bmpOriginal.height, Bitmap.Config.ARGB_8888)
        val tempCanvas = Canvas(bmResult)
        tempCanvas.rotate(
            degrees,
            (bmpOriginal.width / 2).toFloat(),
            (bmpOriginal.height / 2).toFloat()
        )
        tempCanvas.drawBitmap(bmpOriginal, 0f, 0f, null)
        return bmResult
    }

    /**
     *
     * <创建bitmap>
     * @param width
     * @param height
     * @param config
     * @param retryCount 失败尝试次数
     * @return
     * @see [类、类.方法、类.成员]
    </创建bitmap> */
    private fun createBitmapSafely(
        width: Int,
        height: Int,
        config: Bitmap.Config?,
        retryCount: Int
    ): Bitmap? {
        return try {
            Bitmap.createBitmap(width, height, config!!)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(width, height, config, retryCount - 1)
            }
            null
        }
    }

}