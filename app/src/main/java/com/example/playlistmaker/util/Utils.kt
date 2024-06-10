package com.example.playlistmaker.util

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

fun millisecondToMinutesAndSeconds(timeInMilliseconds: String): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMilliseconds.toInt())
}

fun millisecondToMinute(timeInMilliseconds: String): String {
    return SimpleDateFormat("mm", Locale.getDefault()).format(timeInMilliseconds.toInt())
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

fun changeTheEndingOfTheWord(countTrack: Int): String {
    var num: Int = countTrack
    if (num > 100) num %= 100
    if (num in 10..20) return "треков"
    if (num > 20) num %= 10
    return if (num == 1) {
        "трек"
    } else if (num in 2..4) {
        "трека"
    } else {
        "треков"
    }
}

fun changeTheEndingOfTheWordMinute(countMinute: Int): String {
    var num: Int = countMinute
    if (num > 100) num %= 100
    if (num in 10..20) return "минут"
    if (num > 20) num %= 10
    return if (num == 1) {
        "минута"
    } else if (num in 2..4) {
        "минуты"
    } else {
        "минут"
    }
}
