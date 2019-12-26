package com.vaudibert.canidrive.data

import androidx.room.TypeConverter
import java.util.*

/**
 * Static object required for room automatic conversion.
 */
object Converters {

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

}