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
import kotlin.math.pow
import kotlin.math.sqrt


class EqualizerView : View {
    val paint = Paint()
    var leftY: Float = -1F
    var rightY: Float = -1F
    var midPoint: PointF = PointF(0F, 0F)
    var numBands: Int = 5
    private val bezier = Path()


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

    fun getEQLevel(band: Int): Float {
        val x = (band * 2 + 1) * width / (numBands * 2)

        val ax = 0
        val ay = leftY
        val bx = midPoint.x * 2 - width / 2
        val by = midPoint.y * 2 - leftY / 2 - rightY / 2
        val cx = width
        val cy = rightY

        val t1 =
            (ax - bx + sqrt(bx.pow(2) - 2 * x * bx - ax * cx + ax * x + cx * x)) / (ax - 2 * bx + cx)
        val t2 =
            -(ax - bx + sqrt(bx.pow(2) - 2 * x * bx - ax * cx + ax * x + cx * x)) / (ax - 2 * bx + cx)
        val t = if (t1 in 0.0..1.0) t1 else t2

        return 1 - ((1 - t).pow(2)*ay + 2*t*(1 - t)*by + t.pow(2)*cy) / height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.FILL
        if (leftY == -1F || rightY == -1F) {
            leftY = (height/2).toFloat()
            rightY = (height/2).toFloat()
            midPoint.set((width/2).toFloat(), (height/2).toFloat())
        }

        bezier.reset()
        bezier.moveTo(0F, leftY)
        bezier.quadTo(2*midPoint.x - width/2, 2*midPoint.y - leftY/2 - rightY/2,
            width.toFloat(), rightY)
        bezier.lineTo(width.toFloat(), height.toFloat())
        bezier.lineTo(0F, height.toFloat())
        canvas.apply {
            drawPath(bezier, paint)
        }

        for (i in 1 until numBands){
            canvas.drawLine(
                (i * width / numBands).toFloat(), 0F,
                (i * width / numBands).toFloat(), height.toFloat(), paint)
        }
    }
}