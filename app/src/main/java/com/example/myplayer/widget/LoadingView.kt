package com.example.myplayer.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.View.MeasureSpec
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import com.example.myplayer.R


/**
 * @author gaofengpeng
 * @date 2019/9/16
 * @description :
 */
class LoadingView @JvmOverloads constructor(
    context: Context,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    /**
     * 动画的三种颜色
     */
    private var mTopColor = 0
    private var mLeftColor = 0
    private var mRightColor = 0
    private var mPaint: Paint? = null

    /**
     * 扫描角度，用于控制圆弧的长度
     */
    private var sweepAngle = 0f

    /**
     * 开始角度，用于控制圆弧的显示位置
     */
    private var startAngle = 0f

    /**
     * 当前角度，记录圆弧旋转的角度
     */
    private var curStartAngle = 0f

    /**
     * 用动画控制圆弧显示
     */
    private var mValueAnimator: ValueAnimator? = null

    /**
     * 每个周期的时长
     */
    private var mDuration = 0

    /**
     * 圆弧线宽
     */
    private var mStrokeWidth = 0f

    /**
     * 动画过程中最大的圆弧角度
     */
    private var mMaxSweepAngle = 0

    /**
     * 是否自动开启动画
     */
    private var mAutoStart = false

    /**
     * 用于判断当前动画是否处于Reverse状态
     */
    private var mReverse = false
    private fun initAttr(context: Context, attrs: AttributeSet?) {
        mTopColor = ContextCompat.getColor(context, R.color.red)
        mDuration = 1000
        if (mDuration <= 0) {
            mDuration = 1000
        }
        mStrokeWidth = 8f
        mMaxSweepAngle = 360
//        if (mMaxSweepAngle <= 0 || mMaxSweepAngle > 120) {
//            // 对于不规范值直接采用默认值
//            mMaxSweepAngle = 90
//        }
        mAutoStart = true
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.STROKE
        mPaint!!.strokeWidth = mStrokeWidth
        mPaint!!.strokeCap = Paint.Cap.ROUND

        // 最小角度为1度，是为了显示小圆点
        sweepAngle = -1f
        startAngle = -90f
        curStartAngle = startAngle

        // 扩展动画
        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(mDuration.toLong())
        mValueAnimator?.setRepeatMode(ValueAnimator.REVERSE)
        mValueAnimator?.setRepeatCount(ValueAnimator.INFINITE)
        mValueAnimator?.addUpdateListener(AnimatorUpdateListener { animation: ValueAnimator ->
            var fraction = animation.animatedFraction
            val value = animation.animatedValue as Float
            if (mReverse) fraction = 1 - fraction
            startAngle = curStartAngle + fraction * 120
            sweepAngle = -1 - mMaxSweepAngle * value
            postInvalidate()
        })
        mValueAnimator?.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                curStartAngle = startAngle
                mReverse = !mReverse
            }
        })
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var width = MeasureSpec.getSize(widthMeasureSpec)
        var height = MeasureSpec.getSize(heightMeasureSpec)

        // 对于wrap_content ，设置其为20dp。默认情况下wrap_content和match_parent是一样的效果
        if (widthMode == MeasureSpec.AT_MOST) {
            width = (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20f,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            height = (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20f,
                context.resources.displayMetrics
            ) + 0.5f).toInt()
        }
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = measuredWidth
        val height = measuredHeight
        val side = Math.min(
            width - paddingStart - paddingEnd,
            height - paddingTop - paddingBottom
        ) - (mStrokeWidth + 0.5f).toInt()

        // 确定动画位置
        val left = (width - side) / 2f
        val top = (height - side) / 2f
        val right = left + side
        val bottom = top + side

        // 绘制圆弧
        mPaint!!.color = mTopColor
        canvas.drawArc(left, top, right, bottom, startAngle, sweepAngle, false, mPaint!!)
    }

    override fun onDetachedFromWindow() {
        if (mAutoStart && mValueAnimator!!.isRunning) {
            mValueAnimator!!.cancel()
        }
        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {
        if (mAutoStart && !mValueAnimator!!.isRunning) {
            mValueAnimator!!.start()
        }
        super.onAttachedToWindow()
    }

    /**
     * 开始动画
     */
    fun start() {
        if (!mValueAnimator!!.isStarted) {
            mValueAnimator!!.start()
        }
    }

    /**
     * 暂停动画
     */
    fun pause() {
        if (mValueAnimator!!.isRunning) {
            mValueAnimator!!.pause()
        }
    }

    /**
     * 继续动画
     */
    fun resume() {
        if (mValueAnimator!!.isPaused) {
            mValueAnimator!!.resume()
        }
    }

    /**
     * 停止动画
     */
    fun stop() {
        if (mValueAnimator!!.isStarted) {
            mReverse = false
            mValueAnimator!!.end()
        }
    }

    init {
        initAttr(context, attrs)
        init()
    }
}