package com.example.myapplication.custom_views

import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.forEach
import com.example.myapplication.R

class CustomBarChart @JvmOverloads constructor (context: Context,
                      attrs: AttributeSet?,
                      defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var view : ConstraintLayout

    private lateinit var progressPaint: Paint
    private lateinit var backgroundPaint: Paint
    var max = 100


    private val rect = RectF()
    private var diameter = 0F
    private var angle = 0F

    private var llBars : LinearLayoutCompat? = null

    init {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val view = View.inflate(context, R.layout.column_chart, this)
//            LayoutInflater.from(context).inflate(R.layout.vertical_progress_bar)
//            val progessBar = View.inflate(context, R.layout.vertical_progress_bar)
            view?.let {
                llBars = it.findViewById(R.id.ll_bars)
            }
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomBarChart, 0, 0)
            val stroke = typedArray.getDimension(
                R.styleable.CustomBarChart_strokeS, context.resources.getDimension(
                    R.dimen._1dp))
            val backgroundColor = typedArray.getColor(R.styleable.CustomBarChart_chartBackgroundColor, ContextCompat.getColor(context, R.color.teal_200))
            val progressColor = typedArray.getColor(R.styleable.CustomBarChart_chartProgressColor, ContextCompat.getColor(context, R.color.black))
            val barCount = typedArray.getInt(R.styleable.CustomBarChart_barCount, 0)
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
            typedArray.recycle()

            setProgressBars(barCount)
        }
    }

    private fun setProgressBars(barCount: Int) {
        llBars?.run {
            (1..barCount).forEach { _ ->

                this.addView(LayoutInflater.from(context).inflate(R.layout.vertical_progress_bar, null)).apply {
//                    layoutParams = LayoutParams
                }
            }
        }
    }
}