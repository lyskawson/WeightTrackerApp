package com.example.weighttracker.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(millis : Long?, pattern: String = "MMM dd, yyyy"): String? {
    if (millis == null) {
        return null
    }
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(millis))
}

fun parseDateToMillis(dateString: String, pattern: String = "yyyy-MM-dd"): Long? {
    return try {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        formatter.parse(dateString)?.time
    } catch (e: Exception) {
        null
    }
}