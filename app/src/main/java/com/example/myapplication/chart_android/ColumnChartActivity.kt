package com.example.myapplication.chart_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.chart_progress_bar.BarData
import com.example.myapplication.custom_views.CustomBarChartRender
import com.example.myapplication.custom_views.RoundedBarChart
import com.example.myapplication.databinding.ActivityColumnChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.model.GradientColor


class ColumnChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityColumnChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColumnChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBarChart()
        initRoundedBarchart()
    }

    private fun initRoundedBarchart() {
        val labels = ArrayList<String>()
        labels.add("S")
        labels.add("M")
        labels.add("T")
        labels.add("W")
        labels.add("T")
        labels.add("F")
        labels.add("S")

        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(1f, 5f))
        entries.add(BarEntry(2f, 3f))
        entries.add(BarEntry(3f, 8f))
        entries.add(BarEntry(4f, 24f))
        entries.add(BarEntry(5f, 5f))
        entries.add(BarEntry(6f, 9f))
        entries.add(BarEntry(7f, 10f))

        initChart(labels, entries)
    }

    private fun initChart(
        labels: List<String>,
        values: List<BarEntry>
    ) {
        binding.barChart.setDrawBarShadow(false)
        binding.barChart.setDrawValueAboveBar(false)

        binding.barChart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be drawn
        binding.barChart.setMaxVisibleValueCount(values.size)

        // scaling can now only be done on x- and y-axis separately
        binding.barChart.setPinchZoom(false)
        binding.barChart.isDoubleTapToZoomEnabled = false
        binding.barChart.setScaleEnabled(true)

        binding.barChart.setDrawGridBackground(false)

        // binding.barChart.setDrawYLabels(false);
        val xAxisFormatter: ValueFormatter = IndexAxisValueFormatter(labels)
        val xAxis = binding.barChart.xAxis

        xAxis.setLabelCount(values.size, true)
        xAxis.valueFormatter = xAxisFormatter
        xAxis.labelCount = values.size

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.textColor = ContextCompat.getColor(this, R.color.teal_400)
        xAxis.axisLineColor = ContextCompat.getColor(this, R.color.teal_400)

        //val custom: ValueFormatter = MyValueFormatter(Locale.getDefault())

        val leftAxis = binding.barChart.axisLeft
        leftAxis.setLabelCount(3, true)
        //leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 10f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        leftAxis.textColor = ContextCompat.getColor(this, R.color.teal_400)
        leftAxis.axisLineColor = ContextCompat.getColor(this, R.color.teal_400)
        leftAxis.zeroLineColor = ContextCompat.getColor(this, R.color.teal_400)

        binding.barChart.axisRight.isEnabled = false

        /*val rightAxis = binding.barChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setLabelCount(8, false)
        rightAxis.valueFormatter = custom
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)*/


        val l = binding.barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = Legend.LegendForm.NONE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 1f

        /*val mv = CustomMarkerView(context, xAxisFormatter, unit)
        mv.chartView = binding.barChart // For bounds control

        binding.barChart.marker = mv // Set the marker to the chart*/

        /*val roundedBarChartRenderer = CustomBarChartRender(binding.barChart, binding.barChart.animator, binding.barChart.viewPortHandler)
        roundedBarChartRenderer.setRadius(100)
        binding.barChart.renderer = roundedBarChartRenderer*/

        setData(values)
    }

    private fun setData(values: List<BarEntry>) {

        val set1: BarDataSet
        if (binding.barChart.data != null &&
            binding.barChart.data.dataSetCount > 0
        ) {
            set1 = binding.barChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            binding.barChart.data.barWidth = 0.3f
            binding.barChart.data.notifyDataChanged()
            binding.barChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, null)

            set1.setDrawIcons(false)
            set1.setDrawValues(false)
            //set color of bars
            val startColor1 = ContextCompat.getColor(this, R.color.teal_400)
            val endColor1 = ContextCompat.getColor(this, R.color.teal_400)
            val gradientColors: MutableList<GradientColor> = ArrayList()
            gradientColors.add(GradientColor(startColor1, endColor1))
            set1.gradientColors = gradientColors
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = com.github.mikephil.charting.data.BarData(dataSets)
            data.setDrawValues(false)
            data.barWidth = 0.3f
            binding.barChart.data = data
        }

    }

    /////////////////////////////

    private fun initBarChart() {

        val dataList: ArrayList<BarData> = ArrayList()

        var data = BarData("M", 3.4f, "3.4€")
        dataList.add(data)

        data = BarData("T", 3.4f, "3.4€")
        dataList.add(data)

        data = BarData("W", 7f, "8€")
        dataList.add(data)

        data = BarData("T", 1.8f, "1.8€")
        dataList.add(data)

        data = BarData("F", 7.3f, "7.3€")
        dataList.add(data)

        data = BarData("S", 6.2f, "6.2€")
        dataList.add(data)

        data = BarData("S", 3.3f, "3.3€")
        dataList.add(data)

        binding.ChartProgressBar.setDataList(dataList)
        binding.ChartProgressBar.build()

    }
}