package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime
import com.jakewharton.picnic.TextAlignment.MiddleCenter
import com.jakewharton.picnic.table
import com.kurtraschke.gtfsrtdump.TimestampFormatter
import org.davidmoten.text.utils.WordWrap

fun formatAlert(alert: GtfsRealtime.Alert, tf: TimestampFormatter): String {
    val (urlsMap, headersMap, descriptionMap) = alertContentsByLanguage(alert)

    val allLanguages = urlsMap.keys union headersMap.keys union descriptionMap.keys

    return table {
        cellStyle {
            border = true
        }

        row {
            cell("Active Period") {
                rowSpan = alert.activePeriodCount + 1
            }
            cell("Start")
            cell("End")
            cell("") {
                rowSpan = alert.activePeriodCount + 1
                columnSpan = 8
            }

        }

        alert.activePeriodList.forEach { ap ->
            row(tf.formatTimestamp(ap.start), tf.formatTimestamp(ap.end))
        }

        row {
            cell("Informed Entity") {
                rowSpan = alert.informedEntityCount + 2
            }
            cell("") {
                columnSpan = 3
            }
            cell("Trip") {
                columnSpan = 6
            }
            cell("")
        }

        row {
            cell("Agency")
            cell("Route")
            cell("Route Type")
            cell("ID")
            cell("Route")
            cell("Direction")
            cell("Start Date")
            cell("Start Time")
            cell("Schedule Relationship")
            cell("Stop")
        }

        alert.informedEntityList.forEach { ie ->
            row(ie.agencyId, ie.routeId, ie.routeType,
                    ie.trip.tripId, ie.trip.routeId, ie.trip.directionId, ie.trip.startDate, ie.trip.startTime, ie.trip.scheduleRelationship,
                    ie.stopId)
        }

        row {
            cell("Cause")
            cell(alert.cause) {
                columnSpan = 10
            }
        }

        row {
            cell("Effect")
            cell(alert.effect) {
                columnSpan = 10
            }
        }

        row {
            cell("Language") {
                alignment = MiddleCenter
            }
            cell("") {
                columnSpan = 10
            }
        }

        allLanguages.forEach { languageCode ->
            row {
                cell(languageCode) {
                    rowSpan = 6
                }
                cell("URL") {
                    columnSpan = 10
                }

            }

            row {
                cell(urlsMap[languageCode]) {
                    columnSpan = 10
                }
            }

            row {
                cell("Header") {
                    columnSpan = 10
                }
            }

            row {
                cell(headersMap[languageCode]?.let { WordWrap.from(it).maxWidth(60).wrap() }) {
                    columnSpan = 10
                }
            }

            row {
                cell("Description") {
                    columnSpan = 10
                }
            }

            row {
                cell(descriptionMap[languageCode]?.let { WordWrap.from(it).maxWidth(60).wrap() }) {
                    columnSpan = 10
                }
            }
        }

    }.toString()
}