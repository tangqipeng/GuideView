package com.tqp.guideview

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * @author  tangqipeng
 * @date  4/20/21 3:10 PM
 * @email tangqipeng@aograph.com
 */
class GuideParamter {

    internal var mView: View? = null
    internal var mTipView: View? = null
    internal var mShape: Shape? = null
    internal var mCx: Float = 0F
    internal var mCy: Float = 0F
    internal var mPaddingOffset: PaddingOffset? = null
    internal var mOrientation: TipOrientation? = null
    internal var mTipMargin: Int? = null
    internal var mTipHintListener: (() -> Unit)? = null
    internal var mAmount: Float = 0F

    operator fun plus(guideParamter: GuideParamter): MutableList<GuideParamter>{
        return mutableListOf(this, guideParamter)
    }


    class Builder(private var mContext: Context?) {

        private val guideParamter = GuideParamter()

        private var rootView: ViewGroup? = null

        init {
            if (mContext is Activity) {
                rootView = (mContext as Activity).window.decorView as ViewGroup
            } else if (mContext is Fragment){
                rootView = (mContext as Fragment).requireActivity().window.decorView as ViewGroup
            }
        }

        /**
         * 需要指示的view
         */
        fun addHighLightViewById(@IdRes viewId: Int):Builder {
            guideParamter.mView = rootView?.findViewById(viewId)
            return this
        }


        /**
         * 需要指示的view
         */
        fun addHighLightView(view: View):Builder {
            guideParamter.mView = view
            return this
        }

        /**
         * 高亮的形状
         */
        fun setHighLightShap(shape: Shape):Builder{
            guideParamter.mShape = shape
            return this
        }

        /**
         * 高亮的形状
         */
        fun setRoundRectCorners(cx:Float, cy:Float):Builder{
            guideParamter.mCx = cx
            guideParamter.mCy = cy
            return this
        }

        /**
         * 设置高亮边框与要显示的view的边距
         */
        fun setHighLightViewPadding(paddingOffset: PaddingOffset):Builder {
            guideParamter.mPaddingOffset = paddingOffset
            return this
        }
        /**
         * 添加相对应的提示的view
         */
        fun addTipContentView(view: View):Builder{
            guideParamter.mTipView = view
            return this
        }

        /**
         * 添加相对应的提示的view
         */
        fun addTipContentViewById(@LayoutRes viewId: Int):Builder{
            guideParamter.mTipView = LayoutInflater.from(mContext).inflate(viewId, null)
            return this
        }

        /**
         * 设施提示窗口与高亮窗口的距离
         */
        fun setTipViewMargin(margin: Int):Builder {
            guideParamter.mTipMargin = margin
            return this
        }

        /**
         * 采用的指示框的形状
         */
        fun setTipViewOrientation(orientation: TipOrientation):Builder {
            guideParamter.mOrientation = orientation
            return this
        }

        /**
         * 设置每一步的点击事件
         */
        fun setTipHintListener(tipHintListener: () -> Unit):Builder{
            guideParamter.mTipHintListener = tipHintListener
            return this
        }

        /**
         * 设置高亮部分的透明度
         */
        fun setDialogDimAmount(amount:Float):Builder{
            guideParamter.mAmount = amount
            return this
        }

        fun build(): GuideParamter = guideParamter
    }

}