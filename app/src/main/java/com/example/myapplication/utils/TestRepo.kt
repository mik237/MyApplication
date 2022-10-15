package com.example.myapplication.utils

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestRepo @Inject constructor() {
    private var count = 0
    fun printTestRepo() {
        Log.d("TAG", "printTestRepo: running")
    }

    fun getCount(): Int {
        count += 1
        return count
    }
}