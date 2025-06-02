package ru.ilessy.fatfitbutn.fragments.video.core.utils

import android.annotation.SuppressLint

private const val FORMAT_WITH_HOURS = "%02d:%02d:%02d"
private const val FORMAT_WITHOUT_HOURS = "%02d:%02d"

object TimeFormat {
    @SuppressLint("DefaultLocale")
    fun formatTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return if (hours > 0) {
            String.format(FORMAT_WITH_HOURS, hours, minutes, seconds)
        } else {
            String.format(FORMAT_WITHOUT_HOURS, minutes, seconds)
        }
    }
}