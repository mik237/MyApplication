package com.example.myapplication.custom_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.chart_android.BarData;

import java.text.DecimalFormat;

public class BarChartView extends View {

    /**
     * Paint object used to draw things on canvas.
     */
    private Paint mPaint;
    /**
     * Context
     */
    private Context mContext;
    /**
     * Data used to show bar charts
     */
    private BarData[] mDataArray;
    /**
     * Maximum value in the data set
     */
    private float mMaxValueOfData;
    /**
     * width of drawing
     */
    private final int mStrokeWidth = 2;
    /**
     * Font size for legends along X and Y axis in dp.
     */
    private int mAxisFontSize = 14;
    /**
     * Count of legends shown along Y axis
     */
    private int mMaxValueCountOnYAxis = 9;
    /**
     * Distance between Axis and values shown as legend next to it (in px)
     */
    private int mDistanceAxisAndValue;
    /**
     * Maximum width of legends along Y axis
     */
    private int mMaxWidthOfYAxisText;
    /**
     * Maximum width of legends along X axis
     */
    private int mMaxHeightOfXAxisText;

    /**
     * Constuctor.
     *
     * @param context      Context
     * @param attributeSet set of attributes
     */
    public BarChartView(Context context, AttributeSet attributeSet) {

        super(context, attributeSet);
        mContext = context;
        mPaint = new Paint();
        init();
    }

    /**
     * Initialize internal variables
     */
    private void init() {

        mDistanceAxisAndValue = (int) dpToPixels(mContext, 14);
    }

    /**
     * This View will use the given data for drawing bar chart
     *
     * @param barData data to be used for drawing bar chart.
     */
    public void setYAxisData(BarData[] barData) {

        mDataArray = barData;
        mMaxValueOfData = Float.MIN_VALUE;
        for (int index = 0; index < mDataArray.length; index++) {
            if (mMaxValueOfData < mDataArray[index].getValue())
                mMaxValueOfData = mDataArray[index].getValue();
        }
        findMaxWidthOfText(barData);
        invalidate();
    }

    /**
     * Returns the maximum value in the data set.
     *
     * @return Maximum value in the data set.
     */
    public float getMaxValueOfData() {

        return mMaxValueOfData;
    }

    /**
     * Returns maximum width occupied by any of the Y axis values.
     *
     * @return maximum width occupied by any of the Y axis values
     */
    private int getMaxWidthOfYAxisText() {

        return mMaxWidthOfYAxisText;
    }

    /**
     * Calculate the maximum width occupied by any of given bar chart data. Width is calculated
     * based on default font used and size specified in {@link #mAxisFontSize}.
     *
     * @param barDatas data to be used in bar chart
     */
    private void findMaxWidthOfText(BarData[] barDatas) {

        mMaxWidthOfYAxisText = Integer.MIN_VALUE;
        mMaxHeightOfXAxisText = Integer.MIN_VALUE;

        Paint paint = new Paint();
        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(dpToPixels(mContext, mAxisFontSize));

        Rect bounds = new Rect();

        for (int index = 0; index < mDataArray.length; index++) {
            int currentTextWidth =
                    (int) paint.measureText(Float.toString(barDatas[index].getValue()));
            if (mMaxWidthOfYAxisText < currentTextWidth)
                mMaxWidthOfYAxisText = currentTextWidth;

            mPaint.getTextBounds(barDatas[index].getXAxisName(), 0,
                    barDatas[index].getXAxisName().length(), bounds);
            if (mMaxHeightOfXAxisText < bounds.height())
                mMaxHeightOfXAxisText = bounds.height();
        }
    }

    /**
     * Returns the maximum height of X Axis text.
     *
     * @return the maximum height of X Axis text
     */
    public int getMaxHeightOfXAxisText() {

        return mMaxHeightOfXAxisText;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int usableViewHeight = getHeight() - getPaddingBottom() - getPaddingTop();
        int usableViewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        Point origin = getOrigin();
        mPaint.setColor(ContextCompat.getColor(mContext, R.color.purple_700));
        mPaint.setStrokeWidth(mStrokeWidth);
        //draw y axis
        canvas.drawLine(origin.x, origin.y, origin.x,
                origin.y - (usableViewHeight - getXAxisLabelAndMargin()), mPaint);
        //draw x axis
        mPaint.setStrokeWidth(mStrokeWidth + 1);
        canvas.drawLine(origin.x, origin.y,
                origin.x + usableViewWidth -
                        (getMaxWidthOfYAxisText() +
                                mDistanceAxisAndValue), origin.y, mPaint);

        if (mDataArray == null || mDataArray.length == 0)
            return;
        //draw bar chart
        int barAndVacantSpaceCount = (mDataArray.length << 1) + 1;
        int widthFactor = (usableViewWidth - getMaxWidthOfYAxisText()) / barAndVacantSpaceCount;
        int x1, x2, y1, y2;
        float maxValue = getMaxValueOfData();
        for (int index = 0; index < mDataArray.length; index++) {
            x1 = origin.x + ((index << 1) + 1) * widthFactor;
            x2 = origin.x + ((index << 1) + 2) * widthFactor;
            int barHeight = (int) ((usableViewHeight - getXAxisLabelAndMargin()) *
                    mDataArray[index].getValue() / maxValue);
            y1 = origin.y - barHeight;
            y2 = origin.y;
            canvas.drawRect(x1, y1, x2, y2, mPaint);
            showXAxisLabel(origin, mDataArray[index].getXAxisName(), x1 + (x2 - x1) / 2, canvas);
        }
        showYAxisLabels(origin, (usableViewHeight - getXAxisLabelAndMargin()), canvas);
    }

    /**
     * Formats the given float value up to one decimal precision point.
     *
     * @param value float which needs to be formatted
     *
     * @return String in the format "0.0" e.g. 100.0, 11.1
     *

     *
    TODO make it as interface so that developers can implement interface and use this code.
     */
    private String getFormattedValue(float value) {

        DecimalFormat precision = new DecimalFormat("0.0");
        return precision.format(value);
    }

    /**
     * Draws Y axis labels and marker points along Y axis.
     *
     * @param origin           coordinates of origin on canvas
     * @param usableViewHeight view height after removing the padding
     * @param canvas           canvas to draw the chart
     */
    public void showYAxisLabels(Point origin, int usableViewHeight, Canvas canvas) {

        float maxValueOfData = (int) getMaxValueOfData();
        float yAxisValueInterval = usableViewHeight / mMaxValueCountOnYAxis;
        float dataInterval = maxValueOfData / mMaxValueCountOnYAxis;
        float valueToBeShown = maxValueOfData;
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(dpToPixels(mContext, mAxisFontSize));

        //draw all texts from top to bottom
        for (int index = 0; index < mMaxValueCountOnYAxis; index++) {
            String string = getFormattedValue(valueToBeShown);

            Rect bounds = new Rect();
            mPaint.getTextBounds(string, 0, string.length(), bounds);
            int y = (int) ((origin.y - usableViewHeight) + yAxisValueInterval * index);
            canvas.drawLine(origin.x - (mDistanceAxisAndValue >> 1), y, origin.x, y, mPaint);
            y = y + (bounds.height() >> 1);
            canvas.drawText(string, origin.x - bounds.width() - mDistanceAxisAndValue, y, mPaint);
            valueToBeShown = valueToBeShown - dataInterval;
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
    public void showXAxisLabel(Point origin, String label, int centerX, Canvas canvas) {

        Rect bounds = new Rect();
        mPaint.getTextBounds(label, 0, label.length(), bounds);
        int y = origin.y + mDistanceAxisAndValue + getMaxHeightOfXAxisText();
        int x = centerX - bounds.width() / 2;
        mPaint.setTextSize(dpToPixels(mContext, mAxisFontSize));
        mPaint.setTypeface(Typeface.DEFAULT);
        canvas.drawText(label, x, y, mPaint);
    }

    /**
     * Returns the X axis' maximum label height and margin between label and the X axis.
     *
     * @return the X axis' maximum label height and margin between label and the X axis
     */
    private int getXAxisLabelAndMargin() {

        return getMaxHeightOfXAxisText() + mDistanceAxisAndValue;
    }

    /**
     * Returns the origin coordinates in canvas' coordinates.
     *
     * @return origin's coordinates
     */
    public Point getOrigin() {

        if (mDataArray != null) {

            return new Point(getPaddingLeft() + getMaxWidthOfYAxisText() + mDistanceAxisAndValue,
                    getHeight() - getPaddingBottom() - getXAxisLabelAndMargin());
        } else {

            return new Point(getPaddingLeft() + getMaxWidthOfYAxisText() + mDistanceAxisAndValue,
                    getHeight() - getPaddingBottom());
        }
    }

    /**
     * Convert dp value to pixels.
     *
     * @param context Context
     * @param dpValue Value in dip
     *
     * @return Values in pixels
     */
    public static float dpToPixels(Context context, float dpValue) {

        if (context != null) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, metrics);
        }
        return 0;
    }
}
