package com.example.myplayer.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.graphics.RectF




class MyView : View {

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, 0, 0, 300, 300)
    }
    constructor(context: Context?) :super(context)

    constructor(context: Context?, attrs: AttributeSet?):
            super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(160, 60) //这里面是原始的大小，需要重新计算可以修改本行
        //dosomething
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var mPaint = Paint()
        mPaint.alpha = 200
        mPaint.setStrokeWidth(6f)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.color = Color.RED
        if (canvas != null) {
            val oval = RectF() //RectF对象

            oval.left = 10f //左边

            oval.top = 10f //上边

            oval.right = 80f //右边

            oval.bottom = 60f
            canvas.drawArc(oval,250f,-250f,false,mPaint)
            val oval2 = RectF() //RectF对象

            oval2.left = 80f //左边
            oval2.top = 10f //上边
            oval2.right = 150f //右边
            oval2.bottom = 60f
            canvas.drawArc(oval2,-70f,250f,false,mPaint)

            val oval3 = RectF() //RectF对象
            oval3.left = 40f //左边
            oval3.top = 35f //上边
            oval3.right = 45f //右边
            oval3.bottom = 40f
            canvas.drawArc(oval3,0f,360f,true,mPaint)

            val oval4 = RectF() //RectF对象
            oval4.left = 120f //左边
            oval4.top = 35f //上边
            oval4.right = 125f //右边
            oval4.bottom = 40f

            canvas.drawArc(oval4,0f,360f,true,mPaint)

        }


    }
}