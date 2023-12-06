package com.example.playlistmaker.util

import java.text.SimpleDateFormat
import java.util.Locale

fun millisecondToMinute(timeInMilliseconds: String): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(timeInMilliseconds.toInt())
}
