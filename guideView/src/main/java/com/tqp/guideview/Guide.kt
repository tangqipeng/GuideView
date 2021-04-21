package com.tqp.guideview

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

/**
 * @author  tangqipeng
 * @date  4/19/21 11:43 AM
 * @email tangqipeng@aograph.com
 */
class Guide(context: Context) {

    private var guideDialog: GuideDialog = GuideDialog(context)


    companion object {
        /**
         * 会使用activity.window.decorView 作为父view 全屏显示
         */
        fun with(activity: Activity): Guide {
            return Guide(activity)
        }

        /**
         * 会使用会使用fragment依赖的activity的activity.window.decorView 作为父view 全屏显示
         */
        fun with(fragment: Fragment): Guide {
            return Guide(fragment.context!!)
        }

        /**
         * 会使用传进来的viewGroup作为父view
         */
        fun with(view: View): Guide {
            return Guide(view.context)
        }
        

    }

    /**
     * 添加具体的步骤
     */
    fun addGuideParameter(paramter: GuideParamter): Guide {
        guideDialog.addGuideParamter(paramter)
        return this
    }

    /**
     * 设置所有的步骤
     */
    fun setGuideParameters(guideParamters: MutableList<GuideParamter>): Guide {
        guideDialog.setGuideParamters(guideParamters)
        return this
    }

    /**
     * 设置dialog的背景透明度
     */
    fun setGuideDimAmount(amount:Float): Guide  {
        guideDialog.setDialogDimAmount(amount)
        return this
    }


    /**
     * 在最后一步设置监听事件，处理事件
     */
    fun setLastStepListener(lastStepListener: () -> Unit): Guide{
        guideDialog.setGuideLastStepListener(lastStepListener)
        return this
    }

    fun show(){
        guideDialog.showGuide()
    }
    
}