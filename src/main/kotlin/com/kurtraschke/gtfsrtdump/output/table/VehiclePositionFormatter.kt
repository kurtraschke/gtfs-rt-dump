package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.VehiclePosition
import com.jakewharton.picnic.TextAlignment.MiddleLeft
import com.jakewharton.picnic.table
import com.kurtraschke.gtfsrtdump.TimestampFormatter

fun formatVehiclePosition(vp: VehiclePosition, tf: TimestampFormatter): String {
    return table {
        style {
            //borderStyle = BorderStyle.Hidden
        }
        cellStyle {
            border = true
        }

        (if (vp.hasTrip()) vp.trip else null)?.let { trip ->
            formatTripDescriptor(trip, this)
        }

        (if (vp.hasVehicle()) vp.vehicle else null)?.let { vehicle ->
            formatVehicleDescriptor(vehicle, this, 3)
        }

        row {
            cell("Position") {
                rowSpan = 4
                alignment = MiddleLeft
            }
            cell("Latitude")
            cell("Longitude")
            cell("Bearing")
            cell("Odometer")
            cell("Speed")
            cell("") {
                rowSpan = 2
            }
        }
        row(vp.position.latitude, vp.position.longitude, vp.position.bearing, vp.position.odometer, vp.position.speed)

        row {
            cell("Current Status")
            cell("Stop ID")
            cell("Current Stop Sequence")
            cell("Timestamp")
            cell("Congestion Level")
            cell("Occupancy Status")
        }
        row(vp.currentStatus, vp.stopId, vp.currentStopSequence, tf.formatTimestamp(vp.timestamp), vp.congestionLevel, vp.occupancyStatus)
    }.toString()
}