package com.example.weighttracker.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// --- CHANGE HERE: Include the year in the pattern ---
private const val CONSISTENT_DATE_PATTERN = "dd MMM yyyy"

// --- Formatting function (NO CHANGE NEEDED HERE) ---
// This should format based on the user's locale and timezone for display
fun formatTimestampToString(timestamp: Long, pattern: String = CONSISTENT_DATE_PATTERN): String {
    if (timestamp <= 0L) return "Invalid Date"
    return try {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        // Uses default TimeZone for formatting - THIS IS CORRECT for display
        formatter.format(Date(timestamp))
    } catch (e: Exception) {
        Log.e("DateFormatting", "Failed to format timestamp '$timestamp' with pattern '$pattern'", e)
        "Format Error"
    }
}


// --- CHANGE IN PARSING FUNCTION ---
// Parses a date String into a Long timestamp (milliseconds since epoch UTC)
fun parseStringToTimestamp(dateString: String?, pattern: String = CONSISTENT_DATE_PATTERN): Long? {
    if (dateString.isNullOrBlank()) {
        return null
    }
    return try {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        // --- ADD THIS LINE ---
        // Tell the formatter to interpret the input string as if it's a UTC date
        formatter.timeZone = TimeZone.getTimeZone("UTC")

        // Now, parse().time will give milliseconds since epoch for 00:00:00 UTC on that date
        formatter.parse(dateString)?.time
    } catch (e: java.text.ParseException) {
        Log.e("DateParsing", "Failed to parse date string '$dateString' with pattern '$pattern'", e)
        null
    } catch (e: Exception) {
        Log.e("DateParsing", "Unexpected error parsing date string '$dateString'", e)
        null
    }
}

// --- getStartOfTodayUtcMillis (NO CHANGE NEEDED HERE) ---
// This function already correctly calculates the UTC start of today
fun getStartOfTodayUtcMillis(): Long {
    // ... implementation remains the same ...
    val localCalendar = Calendar.getInstance(TimeZone.getDefault())
    val year = localCalendar.get(Calendar.YEAR)
    val month = localCalendar.get(Calendar.MONTH)
    val day = localCalendar.get(Calendar.DAY_OF_MONTH)
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.clear()
    utcCalendar.set(year, month, day, 0, 0, 0)
    utcCalendar.set(Calendar.MILLISECOND, 0)
    return utcCalendar.timeInMillis
}