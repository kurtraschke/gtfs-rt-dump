@file:JvmName("FeedHeaderFormatter")

package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.FeedHeader
import com.jakewharton.picnic.table
import com.kurtraschke.gtfsrtdump.TimestampFormatter

fun formatFeedHeader(fh: FeedHeader, tf: TimestampFormatter): String {
    return table {
        cellStyle {
            border = true
        }
        row("GTFS-realtime version", fh.gtfsRealtimeVersion)
        row("Incrementality", fh.incrementality)
        row("Feed Timestamp", tf.formatTimestamp(fh.timestamp))
    }.toString()
}