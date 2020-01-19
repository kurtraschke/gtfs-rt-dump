package com.kurtraschke.gtfsrtdump

interface TimestampFormatter {
    fun formatTimestamp(posixTimestamp: Long): String
}