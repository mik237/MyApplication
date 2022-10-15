package com.example.myapplication.custom_views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.chart_android.BarData
import java.text.DecimalFormat



class BarChartViewK(
    /**
     * Context
     */
    private val mContext: Context, attributeSet: AttributeSet?
) :
    View(mContext, attributeSet) {
    /**
     * Paint object used to draw things on canvas.
     */
    private val mPaint: Paint

    /**
     * Data used to show bar charts
     */
    private var mDataArray: Array<BarData>? = null
    /**
     * Returns the maximum value in the data set.
     *
     * @return Maximum value in the data set.
     */
    /**
     * Maximum value in the data set
     */
    var maxValueOfData = 0f
        private set

    /**
     * width of drawing
     */
    private val mStrokeWidth = 2

    /**
     * Font size for legends along X and Y axis in dp.
     */
    private val mAxisFontSize = 14

    /**
     * Count of legends shown along Y axis
     */
    private val mMaxValueCountOnYAxis = 9

    /**
     * Distance between Axis and values shown as legend next to it (in px)
     */
    private var mDistanceAxisAndValue = 0
    /**
     * Returns maximum width occupied by any of the Y axis values.
     *
     * @return maximum width occupied by any of the Y axis values
     */
    /**
     * Maximum width of legends along Y axis
     */
    private var maxWidthOfYAxisText = 0
    /**
     * Returns the maximum height of X Axis text.
     *
     * @return the maximum height of X Axis text
     */
    /**
     * Maximum width of legends along X axis
     */
    var maxHeightOfXAxisText = 0
        private set

    /**
     * Initialize internal variables
     */
    private fun init() {
        mDistanceAxisAndValue = dpToPixels(mContext, 14f).toInt()
    }

    /**
     * This View will use the given data for drawing bar chart
     *
     * @param barData data to be used for drawing bar chart.
     */
    fun setYAxisData(barData: Array<BarData>) {
        mDataArray = barData
        maxValueOfData = Float.MIN_VALUE
        for (index in mDataArray!!.indices) {
            if (maxValueOfData < mDataArray!![index].value) maxValueOfData =
                mDataArray!![index].value
        }
        findMaxWidthOfText(barData)
        invalidate()
    }

    /**
     * Calculate the maximum width occupied by any of given bar chart data. Width is calculated
     * based on default font used and size specified in [.mAxisFontSize].
     *
     * @param barDatas data to be used in bar chart
     */
    private fun findMaxWidthOfText(barDatas: Array<BarData>) {
        maxWidthOfYAxisText = Int.MIN_VALUE
        maxHeightOfXAxisText = Int.MIN_VALUE
        val paint = Paint()
        paint.typeface = Typeface.DEFAULT
        paint.textSize = dpToPixels(mContext, mAxisFontSize.toFloat())
        val bounds = Rect()
        for (index in mDataArray!!.indices) {
            val currentTextWidth =
                paint.measureText(java.lang.Float.toString(barDatas[index].value)).toInt()
            if (maxWidthOfYAxisText < currentTextWidth) maxWidthOfYAxisText = currentTextWidth
            mPaint.getTextBounds(
                barDatas[index].XAxisName, 0,
                barDatas[index].XAxisName.length, bounds
            )
            if (maxHeightOfXAxisText < bounds.height()) maxHeightOfXAxisText = bounds.height()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val usableViewHeight = height - paddingBottom - paddingTop
        val usableViewWidth = width - paddingLeft - paddingRight
        val origin = origin
        mPaint.color = ContextCompat.getColor(mContext, R.color.purple_700)
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        //draw y axis
        canvas.drawLine(
            origin.x.toFloat(), origin.y.toFloat(), origin.x.toFloat(), (
                    origin.y - (usableViewHeight - xAxisLabelAndMargin)).toFloat(), mPaint
        )
        //draw x axis
        mPaint.strokeWidth = (mStrokeWidth + 1).toFloat()
        canvas.drawLine(
            origin.x.toFloat(), origin.y.toFloat(), (
                    origin.x + usableViewWidth -
                            (maxWidthOfYAxisText +
                                    mDistanceAxisAndValue)).toFloat(), origin.y.toFloat(), mPaint
        )
        if (mDataArray == null || mDataArray!!.size == 0) return
        //draw bar chart
        val barAndVacantSpaceCount = (mDataArray!!.size shl 1) + 1
        val widthFactor = (usableViewWidth - maxWidthOfYAxisText) / barAndVacantSpaceCount
        var x1: Int
        var x2: Int
        var y1: Int
        var y2: Int
        val maxValue = maxValueOfData
        for (index in mDataArray!!.indices) {
            x1 = origin.x + ((index shl 1) + 1) * widthFactor
            x2 = origin.x + ((index shl 1) + 2) * widthFactor
            val barHeight = ((usableViewHeight - xAxisLabelAndMargin) *
                    mDataArray!![index].value / maxValue).toInt()
            y1 = origin.y - barHeight
            y2 = origin.y
            canvas.drawRect(x1.toFloat(), y1.toFloat(), x2.toFloat(), y2.toFloat(), mPaint)
            showXAxisLabel(origin, mDataArray!![index].XAxisName, x1 + (x2 - x1) / 2, canvas)
        }
        showYAxisLabels(origin, usableViewHeight - xAxisLabelAndMargin, canvas)
    }

    /**
     * Formats the given float value up to one decimal precision point.
     *
     * @param value float which needs to be formatted
     *
     * @return String in the format "0.0" e.g. 100.0, 11.1
     *
     *
     *
     * TODO make it as interface so that developers can implement interface and use this code.
     */
    private fun getFormattedValue(value: Float): String {
        val precision = DecimalFormat("0.0")
        return precision.format(value.toDouble())
    }

    /**
     * Draws Y axis labels and marker points along Y axis.
     *
     * @param origin           coordinates of origin on canvas
     * @param usableViewHeight view height after removing the padding
     * @param canvas           canvas to draw the chart
     */
    fun showYAxisLabels(origin: Point, usableViewHeight: Int, canvas: Canvas) {
        val maxValueOfData = maxValueOfData.toInt().toFloat()
        val yAxisValueInterval = (usableViewHeight / mMaxValueCountOnYAxis).toFloat()
        val dataInterval = maxValueOfData / mMaxValueCountOnYAxis
        var valueToBeShown = maxValueOfData
        mPaint.typeface = Typeface.DEFAULT
        mPaint.textSize = dpToPixels(mContext, mAxisFontSize.toFloat())

        //draw all texts from top to bottom
        for (index in 0 until mMaxValueCountOnYAxis) {
            val string = getFormattedValue(valueToBeShown)
            val bounds = Rect()
            mPaint.getTextBounds(string, 0, string.length, bounds)
            var y = (origin.y - usableViewHeight + yAxisValueInterval * index).toInt()
            canvas.drawLine(
                (origin.x - (mDistanceAxisAndValue shr 1)).toFloat(),
                y.toFloat(),
                origin.x.toFloat(),
                y.toFloat(),
                mPaint
            )
            y = y + (bounds.height() shr 1)
            canvas.drawText(
                string,
                (origin.x - bounds.width() - mDistanceAxisAndValue).toFloat(),
                y.toFloat(),
                mPaint
            )
            valueToBeShown = valueToBeShown - dataInterval
        }
    }

    /**
     * Draws X axis labels.
     *
     * @param origin  coordinates of origin on canvas
     * @param label   label to be drawn below a bar along X axis
     * @param centerX center x coordinate of the given bar
     * @param canvas  canvas to draw the chart
     */
    fun showXAxisLabel(origin: Point, label: String, centerX: Int, canvas: Canvas) {
        val bounds = Rect()
        mPaint.getTextBounds(label, 0, label.length, bounds)
        val y = origin.y + mDistanceAxisAndValue + maxHeightOfXAxisText
        val x = centerX - bounds.width() / 2
        mPaint.textSize = dpToPixels(mContext, mAxisFontSize.toFloat())
        mPaint.typeface = Typeface.DEFAULT
        canvas.drawText(label, x.toFloat(), y.toFloat(), mPaint)
    }

    /**
     * Returns the X axis' maximum label height and margin between label and the X axis.
     *
     * @return the X axis' maximum label height and margin between label and the X axis
     */
    private val xAxisLabelAndMargin: Int
        private get() = maxHeightOfXAxisText + mDistanceAxisAndValue

    /**
     * Returns the origin coordinates in canvas' coordinates.
     *
     * @return origin's coordinates
     */
    val origin: Point
        get() = if (mDataArray != null) {
            Point(
                paddingLeft + maxWidthOfYAxisText + mDistanceAxisAndValue,
                height - paddingBottom - xAxisLabelAndMargin
            )
        } else {
            Point(
                paddingLeft + maxWidthOfYAxisText + mDistanceAxisAndValue,
                height - paddingBottom
            )
        }

    companion object {
        /**
         * Convert dp value to pixels.
         *
         * @param context Context
         * @param dpValue Value in dip
         *
         * @return Values in pixels
         */
        fun dpToPixels(context: Context?, dpValue: Float): Float {
            if (context != null) {
                val metrics = context.resources.displayMetrics
                return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, metrics)
            }
            return 0f
        }
    }

    /**
     * Constuctor.
     *
     * @param context      Context
     * @param attributeSet set of attributes
     */
    init {
        mPaint = Paint()
        init()
    }
}