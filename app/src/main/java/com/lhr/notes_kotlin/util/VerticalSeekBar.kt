package com.lhr.notes_kotlin.util

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent

class VerticalSeekBar : androidx.appcompat.widget.AppCompatSeekBar {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!,attrs,defStyle) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldh, oldw)
    }

    @Synchronized
    override fun setProgress(progress: Int) // it is necessary for calling setProgress on click of a button
    {
        super.setProgress(progress)
        onSizeChanged(width, height, 0, 0)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(c: Canvas) {
        c.rotate(90F) //旋轉
        c.translate(0F, -width.toFloat()) //旋轉，這兩行不可去掉
//        c.translate(0F, 90F) //旋轉，這兩行不可去掉
        super.onDraw(c)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                progress = (max * event.y / height).toInt()
                onSizeChanged(width, height, 0, 0)
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }
}