@file:JvmName("FeedEntityFormatter")

package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.FeedEntity
import com.jakewharton.picnic.table
import com.kurtraschke.gtfsrtdump.TimestampFormatter

fun formatFeedEntities(feedEntities: Collection<FeedEntity>, timestampFormatter: TimestampFormatter): String {
    return table {
        cellStyle {
            border = true
        }
        body {
            feedEntities.forEach { fe ->
                val formattedFeedEntity = when {
                    fe.hasTripUpdate() -> formatTripUpdate(fe.tripUpdate, timestampFormatter)
                    fe.hasVehicle() -> formatVehiclePosition(fe.vehicle, timestampFormatter)
                    fe.hasAlert() -> formatAlert(fe.alert, timestampFormatter)
                    else -> ""
                }

                row {
                    cell("Entity ID")
                    cell(fe.id)
                    cell("Deleted?")
                    cell(fe.isDeleted)
                }
                row {
                    cell(formattedFeedEntity) {
                        columnSpan = 4
                    }
                }
            }
        }
    }.toString()
}