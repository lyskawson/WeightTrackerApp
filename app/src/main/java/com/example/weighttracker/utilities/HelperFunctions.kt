package com.example.weighttracker.utilities

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Calendar // Ensure imported
import java.util.Date
import java.util.Locale
import java.util.TimeZone

// This pattern defines how dates are STORED as strings and displayed
private const val CONSISTENT_DATE_PATTERN = "MMM dd, yyyy"

// Formats UTC millis (from DatePicker) into a local date string for display/storage
// This function should be correct as is.
fun formatDate(millis : Long?, pattern: String = CONSISTENT_DATE_PATTERN): String? {
    if (millis == null) {
        return null
    }
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    // Format the UTC instant according to local timezone rules
    formatter.timeZone = TimeZone.getDefault()
    return formatter.format(Date(millis))
}

// *** REVISED parseDateToMillis ***
// Parses the local date string and returns millis for the START of that day in UTC
fun parseDateToMillis(dateString: String?, pattern: String = CONSISTENT_DATE_PATTERN): Long? {
    if (dateString.isNullOrBlank()) {
        return null
    }
    return try {
        // 1. Parse the string using local settings to understand the intended date
        val localFormatter = SimpleDateFormat(pattern, Locale.getDefault())
        localFormatter.timeZone = TimeZone.getDefault()
        val parsedDate: Date? = localFormatter.parse(dateString)

        parsedDate?.let { date ->
            // 2. Get a calendar representing the parsed local date/time
            val localCalendar = Calendar.getInstance(TimeZone.getDefault())
            localCalendar.time = date

            // 3. Extract Year, Month (0-indexed), Day from the local calendar
            val year = localCalendar.get(Calendar.YEAR)
            val month = localCalendar.get(Calendar.MONTH)
            val day = localCalendar.get(Calendar.DAY_OF_MONTH)

            // 4. Create a clean UTC calendar
            val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utcCalendar.clear() // Clear all fields first

            // 5. Set ONLY the date parts in the UTC calendar. Time defaults to 00:00:00.000
            utcCalendar.set(year, month, day)

            // 6. Return the millis for the precise start of that day in UTC
            utcCalendar.timeInMillis
        }
    } catch (e: Exception) {
        Log.e("DateParsing", "Failed to parse date string '$dateString' with pattern '$pattern'", e)
        null
    }
}

// *** NEW HELPER FUNCTION ***
// Gets the milliseconds for the START of the current day (local time) in UTC
fun getStartOfTodayUtcMillis(): Long {
    // 1. Get calendar for current time in local timezone
    val localCalendar = Calendar.getInstance(TimeZone.getDefault())

    // 2. Extract Year, Month, Day from local calendar
    val year = localCalendar.get(Calendar.YEAR)
    val month = localCalendar.get(Calendar.MONTH)
    val day = localCalendar.get(Calendar.DAY_OF_MONTH)

    // 3. Create a clean UTC calendar
    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.clear()

    // 4. Set date parts based on local today
    utcCalendar.set(year, month, day)

    // 5. Return millis for the start of local today in UTC
    return utcCalendar.timeInMillis
}