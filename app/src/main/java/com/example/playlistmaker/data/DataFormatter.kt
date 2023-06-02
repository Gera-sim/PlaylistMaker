package com.example.playlistmaker.data

import java.text.SimpleDateFormat
import java.util.Locale

class DateFormatter {
    private val trackTimeFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    fun formatTrackTime(timeInMillis: Long): String {
        return trackTimeFormat.format(timeInMillis)
    }

    fun formatYear(releaseDate: String): String? {
        val date = dateFormat.parse(releaseDate)
        return if (date != null) yearFormat.format(date) else null
    }
}