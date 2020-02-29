package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.*
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

data class AlertContents(val urlsByLanguage: Map<String, String>, val headersByLanguage: Map<String, String>, val descriptionsByLanguage: Map<String, String>)

fun alertContentsByLanguage(alert: Alert): AlertContents {
    val urlsMap = alert.url.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" },
            valueTransform = TranslatedString.Translation::getText
    )

    val headersMap = alert.headerText.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" },
            valueTransform = TranslatedString.Translation::getText
    )

    val descriptionMap = alert.descriptionText.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" },
            valueTransform = TranslatedString.Translation::getText
    )

    return AlertContents(urlsMap, headersMap, descriptionMap)
}