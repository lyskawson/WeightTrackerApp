package com.example.weighttracker.utilities

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

//to display the date in the format of "MMM dd, yyyy"
fun formatDate(millis : Long?, pattern: String = "MMM dd, yyyy"): String? {
    if (millis == null) {
        return null
    }
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(Date(millis))
}
// to prefill the date picker
fun parseDateToMillis(dateString: String?, pattern: String = "yyyy-MM-dd"): Long {
    return try {
        if (dateString.isNullOrBlank()) {
            System.currentTimeMillis()
        } else {
            val formatter = SimpleDateFormat(pattern, Locale.getDefault())
            formatter.timeZone = TimeZone.getDefault()
            formatter.parse(dateString)?.time ?: System.currentTimeMillis()
        }
    } catch (e: Exception) {
        System.currentTimeMillis()
    }
}