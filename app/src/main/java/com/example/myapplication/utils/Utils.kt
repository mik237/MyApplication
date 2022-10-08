package com.example.myapplication.utils

import android.content.Context
import com.example.myapplication.BuildConfig

object Utils {

    fun saveStringInSharedPrefs(ctx: Context, key: String, value: String) {
        val editor =
            ctx.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringFromSharedPrefs(ctx: Context, key: String): String? {
        val sp = ctx.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
        return sp.getString(key, "")
    }
}