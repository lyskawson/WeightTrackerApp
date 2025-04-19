package com.example.weighttracker.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar // Ensure imported
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val CONSISTENT_DATE_PATTERN = "dd MMM"

fun formatDate(millis : Long?, pattern: String = CONSISTENT_DATE_PATTERN): String? {
    if (millis == null) {
        return null
    }
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(Date(millis))
}

fun parseDateToMillis(dateString: String?, pattern: String = CONSISTENT_DATE_PATTERN): Long? {
    if (dateString.isNullOrBlank()) {
        return null
    }
    return try {
        val localFormatter = SimpleDateFormat(pattern, Locale.getDefault())
        localFormatter.timeZone = TimeZone.getDefault()
        val parsedDate: Date? = localFormatter.parse(dateString)

        parsedDate?.let { date ->
            val localCalendar = Calendar.getInstance(TimeZone.getDefault())
            localCalendar.time = date

            val year = localCalendar.get(Calendar.YEAR)
            val month = localCalendar.get(Calendar.MONTH)
            val day = localCalendar.get(Calendar.DAY_OF_MONTH)

            val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utcCalendar.clear()

            utcCalendar.set(year, month, day)

            utcCalendar.timeInMillis
        }
    } catch (e: Exception) {
        Log.e("DateParsing", "Failed to parse date string '$dateString' with pattern '$pattern'", e)
        null
    }
}

fun getStartOfTodayUtcMillis(): Long {
    val localCalendar = Calendar.getInstance(TimeZone.getDefault())

    val year = localCalendar.get(Calendar.YEAR)
    val month = localCalendar.get(Calendar.MONTH)
    val day = localCalendar.get(Calendar.DAY_OF_MONTH)
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.clear()
    utcCalendar.set(year, month, day)
    return utcCalendar.timeInMillis
}