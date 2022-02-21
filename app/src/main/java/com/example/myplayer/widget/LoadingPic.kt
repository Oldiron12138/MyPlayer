package com.example.myplayer.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myplayer.R
import android.graphics.Shader

import android.graphics.LinearGradient




class LoadingPic: View {
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, 0, 0, 300, 300)
    }
    constructor(context: Context?) :super(context)

    constructor(context: Context?, attrs: AttributeSet?):
            super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(62, 62) //这里面是原始的大小，需要重新计算可以修改本行
        //dosomething
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var mPaint = Paint()
        mPaint.alpha = 200
        val color0 = ContextCompat.getColor(context, R.color.gloden)
        val color1 = ContextCompat.getColor(context, R.color.gloden1)
        val color2 = ContextCompat.getColor(context, R.color.gloden2)
        val mShader = LinearGradient(
            63f,
            31f,
            21f,
            63f,
            intArrayOf(color2, color1,color0),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.REPEAT
        )
        mPaint.shader = mShader
        mPaint.setStrokeWidth(2f)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.color = ContextCompat.getColor(context, R.color.gloden)
        if (canvas != null) {
            val oval = RectF() //RectF对象

            oval.left = 2f //左边

            oval.top = 2f //上边

            oval.right = 60f //右边

            oval.bottom = 60f
            canvas.drawArc(oval,0f,120f,false,mPaint)

        }

        var mPaint1 = Paint()
        mPaint.alpha = 200
        mPaint1.setStrokeWidth(3f)
        mPaint1.setStyle(Paint.Style.STROKE)
        mPaint1.color = ContextCompat.getColor(context, R.color.gloden2)
        if (canvas != null) {
            val oval = RectF() //RectF对象

            oval.left = 3f //左边

            oval.top = 3f //上边

            oval.right = 60f //右边

            oval.bottom = 60f
            canvas.drawArc(oval,0f,360f,false,mPaint1)

        }




    }
}