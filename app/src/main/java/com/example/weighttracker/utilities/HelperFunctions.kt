package com.example.weighttracker.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(millis : Long?, pattern: String = "EEE MMM"): String? {
    if (millis == null) {
        return null
    }
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(millis))
}