package cau.dururung.dururung

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View


class EqualizerView : View {
    val paint = Paint()
    var leftY: Float = -1F
    var rightY: Float = -1F
    var midPoint: PointF = PointF(0F, 0F)



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Log.d("WNoise", "$event")
        if (event == null) return super.onTouchEvent(event)

        val tenth = width / 10
        val x = event.x
        val y = event.y

        if (x <= tenth || x >= tenth * 9) { // side
            if (x <= width/2) { // left side
                leftY = y
            } else {
                rightY = y
            }
        } else {
            midPoint.set(x, y)
        }

        invalidate()
        performClick()
        return super.onTouchEvent(event)
    }


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        isClickable = true
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.EqualizerView, defStyle, 0
        )
        a.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        if (leftY == -1F || rightY == -1F) {
            leftY = (height/2).toFloat()
            rightY = (height/2).toFloat()
            midPoint.set((width/2).toFloat(), (height/2).toFloat())
        }

        val bezier = Path()
        bezier.moveTo(0F, leftY)
        bezier.quadTo(2*midPoint.x - width/2, 2*midPoint.y - leftY/2 - rightY/2,
            width.toFloat(), rightY)
        bezier.lineTo(width.toFloat(), height.toFloat())
        bezier.lineTo(0F, height.toFloat())

        canvas.apply {
            drawPath(bezier, paint)
        }
    }
}