package com.tqp.guideview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat

/**
 * @author  tangqipeng
 * @date  4/19/21 11:12 AM
 * @email tangqipeng@aograph.com
 */
class GuideView : View {

    private var mTAG: String = GuideView::class.java.name

    private val mPaint = Paint()
    private val mTipPaint = Paint()
    private lateinit var mView: View
    private var mViewLeft: Int = 0
    private var mViewTop: Int = 0
    private var mViewRight: Int = 0
    private var mViewBottom: Int = 0
    private var mShape: Shape = Shape.Rect
    private var mPaddingOffset: PaddingOffset? = null
    private var mTipView: View? = null
    private var mOrientation: TipOrientation = TipOrientation.RIGHT
    private var mTipMargin: Int = 10

    private var tipBitmap: Bitmap? = null
    private var mTipLeft: Float = 0f
    private var mTipTop: Float = 0f


    private var mTipClickListener: (() -> Unit)? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setBackgroundColor(ContextCompat.getColor(context!!, R.color.translucence))
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

        mTipPaint.isAntiAlias = true
    }

    /**
     * 需要加指示的view
     */
    fun addGuideView(view: View) {
        mView = view
    }

    /**
     * 设置相对屏幕的具体坐标
     */
    fun setViewPosition(viewPosition: IntArray){
        mViewLeft = viewPosition[0]
        mViewTop = viewPosition[1] - getStatusBarHeight(context)
        mViewRight = viewPosition[0] + mView.width
        mViewBottom = mViewTop + mView.height
        loge(mTAG, "mView x is ${viewPosition[0]} y is ${viewPosition[1]}")
        loge(mTAG, "mView left is $mViewLeft, top is $mViewTop, right is $mViewRight, bottom is $mViewBottom")
        loge(
            mTAG,
            "mView left is ${mView.left}, top is ${mView.top}, right is ${mView.right}, bottom is ${mView.bottom}"
        )
        loge(mTAG, "mView.width is ${mView.width}, mView.height is ${mView.height}")
    }

    /**
     * 采用的指示框的形状
     */
    fun setHighLightShape(shape: Shape) {
        mShape = shape
    }

    /**
     * 设置高亮边框与要显示的view的边距
     */
    fun setHighLightViewPadding(paddingOffset: PaddingOffset) {
        this.mPaddingOffset = paddingOffset
    }

    private fun getLeftPadding(): Int {
        return if (mPaddingOffset == null) {
            10
        } else {
            mPaddingOffset!!.left
        }
    }

    private fun getTopPadding(): Int {
        return if (mPaddingOffset == null) {
            10
        } else {
            mPaddingOffset!!.top
        }
    }

    private fun getRightPadding(): Int {
        return if (mPaddingOffset == null) {
            10
        } else {
            mPaddingOffset!!.right
        }
    }

    private fun getBottomPadding(): Int {
        return if (mPaddingOffset == null) {
            10
        } else {
            mPaddingOffset!!.bottom
        }
    }

    /**
     * 添加相对应的提示的view
     */
    fun addTipContentView(view: View) {
        this.mTipView = view
        mTipView!!.post {
            val globeRect = Rect()
            mTipView!!.getLocalVisibleRect(globeRect)
            loge(mTAG, "mTipView width is ${mTipView!!.width}")
            loge(mTAG, "mTipView height is ${mTipView!!.height}")
        }
    }

    /**
     * 采用的指示框的形状
     */
    fun setTipViewOrientation(orientation: TipOrientation) {
        mOrientation = orientation
    }

    /**
     * 设施提示窗口与高亮窗口的距离
     */
    fun setTipViewMargin(margin: Int) {
        this.mTipMargin = margin
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawHightLightArea(canvas)
        drawTipsView(canvas)
    }

    private fun drawHightLightArea(canvas: Canvas?) {
        when (mShape) {
            Shape.Rect -> {
                canvas?.drawRect(
                    (mViewLeft - getLeftPadding()).toFloat(),
                    (mViewTop - getTopPadding()).toFloat(),
                    (mViewRight + getRightPadding()).toFloat(),
                    (mViewBottom + getBottomPadding()).toFloat(),
                    mPaint
                )
            }
            Shape.RoundRect -> {
                val rectF = RectF(
                    (mViewLeft - getLeftPadding()).toFloat(),
                    (mViewTop - getTopPadding()).toFloat(),
                    (mViewRight + getRightPadding()).toFloat(),
                    (mViewBottom + getBottomPadding()).toFloat()
                )
                canvas?.drawRoundRect(rectF, 10f, 10f, mPaint)
            }
            Shape.Circle -> {
                canvas?.drawCircle(
                    (mViewLeft + mView.width/2).toFloat(),
                    (mViewTop + mView.height/2).toFloat(),
                    (mView.width / 2 + getLeftPadding()).toFloat(),
                    mPaint
                )
            }
            Shape.Oval -> {
                val rectF = RectF(
                    (mViewLeft - getLeftPadding()).toFloat(),
                    (mViewTop - getTopPadding()).toFloat(),
                    (mViewRight + getRightPadding()).toFloat(),
                    (mViewBottom + getBottomPadding()).toFloat()
                )
                loge(mTAG, "Oval rectF left is ${rectF.left}, top is ${rectF.top}, right is ${rectF.right}, bottom is ${rectF.bottom}.")
                canvas?.drawOval(rectF, mPaint)
            }
        }
    }

    private fun drawTipsView(canvas: Canvas?) {
        if (mTipView != null) {
            tipBitmap = BitmapUtil.convertViewToBitmap(mTipView!!)
            if (tipBitmap != null) {
                loge(mTAG, "mOrientation is ${mOrientation.ordinal}")
                loge(mTAG, "tipBitmap width is ${tipBitmap!!.width}")
                loge(mTAG, "tipBitmap height is ${tipBitmap!!.height}")
                mTipView!!.background = ContextCompat.getDrawable(
                    context!!,
                    R.drawable.indicate_right_top_bg
                )
                mTipView!!.setPadding(dp2px(10), dp2px(10), dp2px(10), dp2px(10))
                if (mOrientation == TipOrientation.LEFT) {//提示窗在左边，箭头指向右侧
                    if (getUsableDisplayHeight(context!!) - mViewBottom < tipBitmap!!.height - mView.height) {
                        mTipView!!.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.indicate_bottom_right_bg
                        )
                        logd(mTAG, "tipView arrows is bottom")
                        //箭头在右下角，指向右
                        mTipTop = when (mShape) {
                            Shape.Circle -> {
                                (mViewBottom + mView.width / 2 - mView.height / 2 - tipBitmap!!.height + getBottomPadding()).toFloat()
                            }
                            else -> {
                                (mViewBottom - tipBitmap!!.height + getBottomPadding() - mTipMargin).toFloat()
                            }
                        }
                    } else {
                        mTipView!!.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.indicate_top_right_bg
                        )
                        logd(mTAG, "tipView arrows is top")
                        //箭头在右上角，指向右
                        mTipTop = if (mShape == Shape.Circle) {
                            if (mView.width < tipBitmap!!.height) {
                                (mViewTop - mView.width / 2 + mView.height / 2 - getLeftPadding()).toFloat()
                            } else {
                                (mViewTop - tipBitmap!!.height / 2 + mView.height / 2).toFloat()
                            }
                        } else {
                            (mViewTop - getTopPadding()).toFloat()
                        }
                    }
                    mTipLeft = (mViewLeft - tipBitmap!!.width - getLeftPadding() - mTipMargin).toFloat()

                } else if (mOrientation == TipOrientation.TOP) {//提示框在上面
                    mTipLeft = if (mView.width > tipBitmap!!.width) {
                        logd(mTAG, "bottom tipView arrows is left")
                        mTipView!!.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.indicate_left_bottom_bg
                        )
                        (mViewLeft + mView.width/2 - tipBitmap!!.width/2).toFloat()
                    } else {
                        if (getDisplayWidth() - mView.right < (tipBitmap!!.width - mView.width)) {
                            mTipView!!.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.indicate_right_bottom_bg
                            )
                            logd(mTAG, "bottom tipView arrows is right")
                            //箭头在右下角，指向下
                            (mViewRight - tipBitmap!!.width + getLeftPadding()).toFloat()
                        } else {
                            mTipView!!.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.indicate_left_bottom_bg
                            )
                            logd(mTAG, "bottom tipView arrows is left")
                            //箭头在左下角，指向下
                            (mViewLeft - getLeftPadding()).toFloat()
                        }
                    }

                    mTipTop = when (mShape) {
                        Shape.Circle -> {
                            logd(mTAG, "TipView Circle mTipMargin is $mTipMargin")
                            (mViewTop + mView.height/2 - tipBitmap!!.height - (mView.width / 2) - getTopPadding() - mTipMargin).toFloat()
                        }
                        else -> {
                            (mViewTop - tipBitmap!!.height - getTopPadding() - mTipMargin).toFloat()
                        }
                    }
                } else if (mOrientation == TipOrientation.RIGHT) {//提示框在右边
                    mTipLeft = (mViewRight + getRightPadding() + mTipMargin).toFloat()
                    if (getUsableDisplayHeight(context!!) - mViewBottom < tipBitmap!!.height - mView.height) {
                        mTipView!!.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.indicate_bottom_left_bg
                        )
                        logd(mTAG, "tipView arrows is bottom")
                        // 箭头在左下角，指向左
                        mTipTop = when (mShape) {
                            Shape.Circle -> {
                                (mViewBottom + mView.width / 2 - mView.height / 2 - tipBitmap!!.height + getBottomPadding()).toFloat()
                            }
                            else -> {
                                (mViewBottom - tipBitmap!!.height + getBottomPadding()).toFloat()
                            }
                        }
                    } else {
                        mTipView!!.background = ContextCompat.getDrawable(
                            context!!,
                            R.drawable.indicate_top_left_bg
                        )
                        logd(mTAG, "tipView arrows is top")
                        //箭头在左上角，指向左
                        mTipTop = if (mShape == Shape.Circle) {
                            if (mView.width < tipBitmap!!.height) {
                                (mViewTop - mView.width / 2 + mView.height / 2).toFloat()
                            } else {
                                (mViewTop - tipBitmap!!.height / 2 + mView.height / 2).toFloat()
                            }
                        } else {
                            (mViewTop - getTopPadding()).toFloat()
                        }
                    }
                } else {//底部
                    mTipLeft = if (mView.width > tipBitmap!!.width) {
                        mTipView!!.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.indicate_left_top_bg
                        )
                        //箭头在左上角
                        logd(mTAG, "tipView arrows is left")
                        (mViewLeft + mView.width/2 - tipBitmap!!.width/2).toFloat()
                    } else {
                        if (getDisplayWidth() - mViewRight < (tipBitmap!!.width - mView.width)) {
                            mTipView!!.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.indicate_right_top_bg
                            )
                            //箭头在右上角
                            logd(mTAG, "tipView arrows is right")
                            (mViewRight - tipBitmap!!.width + getRightPadding()).toFloat()
                        } else {
                            mTipView!!.background = ContextCompat.getDrawable(
                                context!!,
                                R.drawable.indicate_left_top_bg
                            )
                            //箭头在左上角
                            logd(mTAG, "tipView arrows is left")
                            (mViewLeft - getLeftPadding()).toFloat()
                        }
                    }
                    mTipTop = when (mShape) {
                        Shape.Circle -> {
                            (mViewTop + mView.height/2 + mView.width/2 + getBottomPadding() + mTipMargin).toFloat()
                        }
                        else -> {
                            (mViewBottom + getBottomPadding() + mTipMargin).toFloat()
                        }
                    }
                }
                tipBitmap = BitmapUtil.convertViewToBitmap(mTipView!!)
                if (tipBitmap != null) {
                    if (mTipTop + tipBitmap!!.height > getUsableDisplayHeight(context)){
                        mTipTop = (getUsableDisplayHeight(context) - tipBitmap!!.height - 10).toFloat()
                    } else if (mTipTop < 0){
                        mTipTop = 10F
                    }
                    if (mTipLeft < 0){
                        mTipLeft = 10F
                    } else if (mTipLeft + tipBitmap!!.width > getDisplayWidth()){
                        mTipLeft = (getDisplayWidth() - tipBitmap!!.width - 10).toFloat()
                    }
                    canvas?.drawBitmap(tipBitmap!!, mTipLeft, mTipTop, mTipPaint)
                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val pointx = event!!.x
        val pointy = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (pointx >= mTipLeft && pointx <= (mTipLeft + tipBitmap!!.width) && pointy >= mTipTop && pointy <= mTipTop + tipBitmap!!.height) {
                    mTipClickListener?.invoke()
                }
            }
            MotionEvent.ACTION_UP -> logd(mTAG, "ACTION_UP")
        }
        return super.onTouchEvent(event)
    }

    fun setOnTipClickListener(tipClickListener: () -> Unit) {
        this.mTipClickListener = tipClickListener
    }

}