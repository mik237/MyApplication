package com.example.myapplication.custom_views

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.R

class TextDrawable(
    private val context: Context,
    private val text: String,
    @ColorInt private val bgColor: Int,
    @ColorInt private val textColor: Int,
    private val cornerRadius: Float
) : Drawable() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bgColor
    }

    override fun draw(canvas: Canvas) {

        val rectF = RectF(0F, 0F, bounds.width().toFloat(), bounds.height().toFloat())
        canvas.drawRoundRect(
            rectF,
            cornerRadius,
            cornerRadius,
            paint
        )


        val typeFace = ResourcesCompat.getFont(context, R.font.heading_bold)

        paint.typeface = typeFace
        paint.color = textColor
        paint.textSize = 200F
        paint.textAlign = Paint.Align.CENTER
        val xPosition = bounds.width() / 2
        val yPosition = (bounds.height() / 2 - (paint.descent() + paint.ascent()) / 2).toInt()
        canvas.drawText(text, xPosition.toFloat(), yPosition.toFloat(), paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}