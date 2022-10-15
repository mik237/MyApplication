package com.example.myapplication.custom_views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private lateinit var progressPaint: Paint
    private lateinit var backgroundPaint: Paint
    var max = MAX_PROGRESS
    var progress = START_PROGRESS
        set(value) {
            angle = calculateAngle(value)
            invalidate()
        }

    private val rect = RectF()
    private var diameter = 0F
    private var angle = 0F

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CircularProgressView, 0, 0)
            val stroke = typedArray.getDimension(R.styleable.CircularProgressView_stroke, context.resources.getDimension(R.dimen._1dp))
            val backgroundColor = typedArray.getColor(R.styleable.CircularProgressView_backgroundColor, ContextCompat.getColor(context, R.color.teal_200))
            val progressColor = typedArray.getColor(R.styleable.CircularProgressView_progressColor, ContextCompat.getColor(context, R.color.black))
            progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = stroke
                color = progressColor
                strokeCap = Paint.Cap.ROUND
            }
            backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = stroke
                color = backgroundColor
            }
            max = typedArray.getFloat(R.styleable.CircularProgressView_max, MAX_PROGRESS)
            progress = typedArray.getFloat(R.styleable.CircularProgressView_progress, START_PROGRESS)
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawCircle(MAX_ANGLE, canvas, backgroundPaint)
        drawCircle(angle, canvas, progressPaint)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        diameter = width.coerceAtMost(height).toFloat()
        updateRect()
    }

    fun setProgressColor(@ColorRes color: Int) {
        progressPaint.color = ContextCompat.getColor(context, color)
        invalidate()
    }

    fun setProgressBackgroundColor(@ColorRes color: Int) {
        backgroundPaint.color = ContextCompat.getColor(context, color)
        invalidate()
    }

    private fun updateRect() {
        val strokeWidth = backgroundPaint.strokeWidth
        rect.set(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth)
    }

    private fun drawCircle(angle: Float, canvas: Canvas, paint: Paint) {
        canvas.drawArc(rect, START_ANGLE, angle, false, paint)
    }

    private fun calculateAngle(progress: Float) = MAX_ANGLE / max * progress

    companion object {
        private const val START_ANGLE = -90F
        private const val MAX_ANGLE = 360F
        private const val MAX_PROGRESS = 100F
        private const val START_PROGRESS = 0F
    }
}