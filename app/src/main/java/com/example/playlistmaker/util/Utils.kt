package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

fun millisecondToMinute(timeInMilliseconds: String): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMilliseconds.toInt())
}

fun getYearFromString(date: String?): String {
    val year = date?.split("-".toRegex())?.dropLastWhile { it.isEmpty() }?.toTypedArray()
    return year?.get(0) ?: ""
}

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}
