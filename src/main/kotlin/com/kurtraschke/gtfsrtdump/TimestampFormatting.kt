package com.kurtraschke.gtfsrtdump

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

interface TimestampFormatter {
    fun formatTimestamp(posixTimestamp: Long): String
}

@Suppress("unused")
enum class TimestampFormatting : TimestampFormatter {
    POSIX {
        override fun formatTimestamp(posixTimestamp: Long): String {
            return when (posixTimestamp) {
                0L -> ""
                else -> posixTimestamp.toString()
            }
        }
    },
    ISO_8601_LOCAL {
        override fun formatTimestamp(posixTimestamp: Long): String {
            return when (posixTimestamp) {
                0L -> ""
                else -> DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        .format(Instant.ofEpochSecond(posixTimestamp).atZone(TimeZone.getDefault().toZoneId()))
            }
        }
    },
    ISO_8601_UTC {
        override fun formatTimestamp(posixTimestamp: Long): String {
            return when (posixTimestamp) {
                0L -> ""
                else -> DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        .withZone(ZoneId.of("UTC"))
                        .format(Instant.ofEpochSecond(posixTimestamp)) + "Z"
            }
        }
    }
}