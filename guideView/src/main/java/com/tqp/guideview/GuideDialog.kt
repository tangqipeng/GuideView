package com.tqp.guideview

import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.WindowManager


/**
 * @author  tangqipeng
 * @date  4/19/21 11:47 AM
 * @email tangqipeng@aograph.com
 */
class GuideDialog(private var mContext: Context) {

    private var mTAG: String = GuideDialog::class.java.name

    private var mGuideParamterList: MutableList<GuideParamter> = mutableListOf()

    private var mCurIndex: Int = 0
    private var mAmount: Float = 0F
    private var mLastStepListener: (() -> Unit)? = null

    private fun createDialog(guideParamter: GuideParamter) {
        val dialog = Dialog(mContext, R.style.GuideDialogStyle1)
        val view = GuideView(mContext)
        if (guideParamter.mView != null) {
            view.addGuideView(guideParamter.mView!!)
            if (guideParamter.mShape != null){
                view.setHighLightShape(guideParamter.mShape!!)
            }
            if (guideParamter.mPaddingOffset != null){
                view.setHighLightViewPadding(guideParamter.mPaddingOffset!!)
            }
            if (guideParamter.mTipView != null) {
                view.addTipContentView(guideParamter.mTipView!!)
                if (guideParamter.mOrientation != null) {
                    view.setTipViewOrientation(guideParamter.mOrientation!!)
                } else {
                    throw Exception("请确认提示信息对于高亮部分的方位")
                }
                if (guideParamter.mTipMargin != null){
                    view.setTipViewMargin(guideParamter.mTipMargin!!)
                }
                view.setOnTipClickListener {
                    dialog.dismiss()
                    if (guideParamter.mTipHintListener != null){
                        guideParamter.mTipHintListener?.invoke()
                    }
                    mCurIndex ++
                    showGuide()
                }
                dialog.setContentView(view)
                guideParamter.mView!!.postDelayed({
                    val rect = Rect()
                    guideParamter.mView!!.getGlobalVisibleRect(rect)

                    val position = IntArray(2)
                    guideParamter.mView!!.getLocationOnScreen(position)
                    view.setViewPosition(position)

                    val wd = dialog.window
                    val lp = wd?.attributes
                    if (lp != null) {
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT
                        lp.height = WindowManager.LayoutParams.MATCH_PARENT
                    }
                    if (wd != null) {
                        if (guideParamter.mAmount != 0F) {
                            wd.setDimAmount(guideParamter.mAmount)//背景透明度
                        } else {
                            wd.setDimAmount(mAmount)//背景透明度
                        }
                        wd.attributes = lp
                    }
                    dialog.show()
                }, 200)
            }


        } else {
            loge(mTAG, "mView is null")
        }
    }

    fun showGuide() {
        if (mGuideParamterList.size > 0 && mCurIndex < mGuideParamterList.size){
            val guideParamter = mGuideParamterList[mCurIndex]
            createDialog(guideParamter)
        } else {
            logd(mTAG, "最后一步了")
            if (this.mLastStepListener != null){
                this.mLastStepListener?.invoke()
            }
        }
    }

    /**
     * 一次设置多个步骤
     */
    fun setGuideParamters(guideParamters: MutableList<GuideParamter>){
        this.mGuideParamterList.addAll(guideParamters)
    }

    /**
     * 添加单个步骤，可多次添加
     */
    fun addGuideParamter(guideParamter: GuideParamter){
        this.mGuideParamterList.add(guideParamter)
    }

    /**
     * 设置dialog的背景透明度
     */
    fun setDialogDimAmount(amount:Float) {
        this.mAmount = amount
    }

    /**
     * 设置最后一步后的操作
     */
    fun setGuideLastStepListener(lastStepListener:() -> Unit){
        this.mLastStepListener = lastStepListener
    }

}
