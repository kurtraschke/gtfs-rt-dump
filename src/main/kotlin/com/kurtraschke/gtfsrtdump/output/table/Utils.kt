package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.TripDescriptor
import com.google.transit.realtime.GtfsRealtime.VehicleDescriptor
import com.jakewharton.picnic.TableDsl
import com.jakewharton.picnic.TextAlignment.MiddleLeft

fun formatTripDescriptor(td: TripDescriptor, table: TableDsl, paddingCols: Int = 0) {
    table.apply {
        row {
            cell("Trip") {
                rowSpan = 2
                alignment = MiddleLeft
            }
            cell("Trip")
            cell("Route")
            cell("Direction")
            cell("Start Date")
            cell("Start Time")
            cell("Schedule Relationship")
            cell("") {
                rowSpan = 2
                columnSpan = paddingCols
            }

        }

        row(td.tripId, td.routeId, td.directionId, td.startDate, td.startTime, td.scheduleRelationship)
    }
}

fun formatVehicleDescriptor(vd: VehicleDescriptor, table: TableDsl, paddingCols: Int = 0) {
    table.apply {
        row {
            cell("Vehicle") {
                rowSpan = 2
                alignment = MiddleLeft
            }
            cell("ID")
            cell("Label")
            cell("License Plate")
            cell("") {
                rowSpan = 2
                columnSpan = paddingCols
            }
        }

        row(vd.id, vd.label, vd.licensePlate)
    }
}
