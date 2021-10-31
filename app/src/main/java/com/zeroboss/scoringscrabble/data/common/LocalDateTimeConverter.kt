package com.zeroboss.scoring500.data.common

import io.objectbox.converter.PropertyConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class LocalDateTimeConverter : PropertyConverter<LocalDateTime?, Long?> {
    override fun convertToEntityProperty(time: Long?): LocalDateTime? {
        return time?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneOffset.UTC)
        }
    }

    override fun convertToDatabaseValue(time: LocalDateTime?): Long? {
        return time!!.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }
}

fun getDate(
    date: LocalDateTime
) : String {
    return date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
}