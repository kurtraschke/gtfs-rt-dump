package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.TripUpdate
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.table
import com.kurtraschke.gtfsrtdump.utils.TimestampFormatter

fun formatTripUpdate(tu: TripUpdate, tf: TimestampFormatter): String {
    return table {
        style {
            //borderStyle = Hidden
        }
        cellStyle {
            border = true
        }

        (if (tu.hasTrip()) tu.trip else null)?.let { trip ->
            formatTripDescriptor(trip, this, 2)
        }

        (if (tu.hasVehicle()) tu.vehicle else null)?.let { vehicle ->
            formatVehicleDescriptor(vehicle, this, 5)
        }

        row {
            cell("Timestamp")
            cell(tf.formatTimestamp(tu.timestamp))
            cell("Delay")
            cell(tu.delay)
            cell("") {
                columnSpan = 5
            }
        }

        if (tu.stopTimeUpdateList.isNotEmpty()) {
            row {
                cell("") {
                    columnSpan = 2
                }
                cell("Arrival") {
                    columnSpan = 3
                    alignment = MiddleCenter
                }
                cell("Departure") {
                    columnSpan = 3
                    alignment = MiddleCenter
                }
                cell("")
            }

            row(
                "Stop ID",
                "Stop Sequence",
                "Time",
                "Delay",
                "Uncertainty",
                "Time",
                "Delay",
                "Uncertainty",
                "Schedule Relationship"
            )

            tu.stopTimeUpdateList.forEach { stu ->
                row(
                    stu.stopId,
                    stu.stopSequence,
                    tf.formatTimestamp(stu.arrival.time),
                    stu.arrival.delay,
                    stu.arrival.uncertainty,
                    tf.formatTimestamp(stu.departure.time),
                    stu.departure.delay,
                    stu.departure.uncertainty,
                    stu.scheduleRelationship
                )
            }
        }

    }.toString()
}

