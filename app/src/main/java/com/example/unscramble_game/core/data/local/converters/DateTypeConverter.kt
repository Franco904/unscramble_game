package com.example.unscramble_game.core.data.local.converters

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {
    @TypeConverter
    fun fromDateToLongTimestamp(date: Date): Long = date.time

    @TypeConverter
    fun fromLongTimestampToDate(timestamp: Long): Date = Date(timestamp)
}
