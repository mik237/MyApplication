package com.example.myapplication.chart_android

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMpchartBinding






class MPChartActivity : AppCompatActivity() {




    private lateinit var binding: ActivityMpchartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMpchartBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setBarChartData()
    }


    private fun setBarChartData() {
        val arrayList = mutableListOf<BarData>()
//        val ta = arl.toTypedArray()
//        val arrayList = ArrayList<BarData>()
        var barData = BarData("Jan", 11.1f)
        arrayList.add(barData)
        barData = BarData("Feb", 8f)
        arrayList.add(barData)
        barData = BarData("Mar", 20f)
        arrayList.add(barData)
        /*barData = BarData("Apr", 43f)
        arrayList.add(barData)
        barData = BarData("May", 60f)
        arrayList.add(barData)
        barData = BarData("Jun", 12.5f)
        arrayList.add(barData)
        barData = BarData("July", 7.5f)
        arrayList.add(barData)
        barData = BarData("July", 7.5f)
        arrayList.add(barData)*/
/*float[] arr =
  new float[]{11.1f, 8f, 100f, 7.5f, 3.5f, 2.5f, 10f*//*, 100f, 7.5f, 3.5f, 2.5f, 10f,
    100f, 7.5f, 3.5f,75f,50f,25f, 7.5f, 3.5f*//*};*/
/*float[] arr =
  new float[]{11.1f, 8f, 100f, 7.5f, 3.5f, 2.5f, 10f*/
        /*, 100f, 7.5f, 3.5f, 2.5f, 10f,
    100f, 7.5f, 3.5f,75f,50f,25f, 7.5f, 3.5f*/
        /*};*/
        val arrayBarData: Array<BarData> = arrayList.toTypedArray()
        binding.barChart.setYAxisData(arrayBarData)
    }
}


data class BarData(val XAxisName: String, val value: Float)