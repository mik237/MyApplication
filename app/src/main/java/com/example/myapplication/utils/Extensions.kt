package com.example.myapplication.utils

import android.content.res.Resources
import com.example.myapplication.utils.Extensions.dp
import com.example.myapplication.utils.Extensions.px


object Extensions {
    val Int.dp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
    val Int.px: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    val Int.dpToPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    val Int.pxToDp: Int
        get() = (this / Resources.getSystem().displayMetrics.density).toInt()
}