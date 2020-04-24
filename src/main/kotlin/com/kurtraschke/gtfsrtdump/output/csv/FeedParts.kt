package com.kurtraschke.gtfsrtdump.output.csv

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.google.transit.realtime.GtfsRealtime.*
import com.google.transit.realtime.GtfsRealtime.TripUpdate.StopTimeUpdate
import com.kurtraschke.gtfsrtdump.alertContentsByLanguage

internal interface CsvOutput {
    fun generateOutput(fm: FeedMessage)
}

fun makeStopTimeUpdateRow(fe: FeedEntity, tu: TripUpdate, trip: TripDescriptor, vehicle: VehicleDescriptor, stu: StopTimeUpdate): Array<Any> {
    return arrayOf(
            fe.id,
            fe.isDeleted,
            trip.tripId,
            trip.routeId,
            trip.directionId,
            trip.startDate,
            trip.startTime,
            trip.scheduleRelationship,
            vehicle.id,
            vehicle.label,
            vehicle.licensePlate,
            tu.timestamp,
            tu.delay,
            stu.stopId,
            stu.stopSequence,
            stu.scheduleRelationship,
            stu.arrival.time,
            stu.arrival.delay,
            stu.arrival.uncertainty,
            stu.departure.time,
            stu.departure.delay,
            stu.departure.uncertainty)
}

@Suppress("unused")
enum class FeedParts : CsvOutput {
    FEED_HEADER {
        override fun generateOutput(fm: FeedMessage) {
            val schema = CsvSchema.builder()
                    .setUseHeader(true)
                    .addColumn("version")
                    .addColumn("timestamp")
                    .addColumn("incrementality")
                    .build()

            val writer = CsvMapper().writerFor(Array<Any>::class.java).with(schema)
            val sw = writer.writeValues(System.out)

            val fh = fm.header

            sw.write(arrayOf(fh.gtfsRealtimeVersion, fh.timestamp, fh.incrementality))
        }
    },
    TRIP_UPDATES {
        override fun generateOutput(fm: FeedMessage) {
            val schema = CsvSchema.builder()
                    .setUseHeader(true)
                    .addColumn("feedEntity.id")
                    .addColumn("feedEntity.deleted")
                    .addColumn("trip.tripId")
                    .addColumn("trip.routeId")
                    .addColumn("trip.directionId")
                    .addColumn("trip.startDate")
                    .addColumn("trip.startTime")
                    .addColumn("trip.scheduleRelationship")
                    .addColumn("vehicle.id")
                    .addColumn("vehicle.label")
                    .addColumn("vehicle.licensePlate")
                    .addColumn("tripUpdate.timestamp")
                    .addColumn("tripUpdate.delay")
                    .addColumn("stopTimeUpdate.stopId")
                    .addColumn("stopTimeUpdate.stopSequence")
                    .addColumn("stopTimeUpdate.scheduleRelationship")
                    .addColumn("stopTimeUpdate.arrival.time")
                    .addColumn("stopTimeUpdate.arrival.delay")
                    .addColumn("stopTimeUpdate.arrival.uncertainty")
                    .addColumn("stopTimeUpdate.departure.time")
                    .addColumn("stopTimeUpdate.departure.delay")
                    .addColumn("stopTimeUpdate.departure.uncertainty")
                    .build()

            val writer = CsvMapper().writerFor(Array<Any>::class.java).with(schema)
            val sw = writer.writeValues(System.out)

            fm.entityList
                    .filter(FeedEntity::hasTripUpdate)
                    .flatMap { fe ->
                        val tu = fe.tripUpdate
                        val trip = tu.trip
                        val vehicle = tu.vehicle

                        if (fe.tripUpdate.stopTimeUpdateList.isNotEmpty()) {
                            fe.tripUpdate.stopTimeUpdateList.map { stu ->
                                makeStopTimeUpdateRow(fe, tu, trip, vehicle, stu)
                            }
                        } else {
                            listOf(makeStopTimeUpdateRow(fe, tu, trip, vehicle, StopTimeUpdate.getDefaultInstance()))
                        }
                    }
                    .forEach { sw.write(it) }
        }
    },
    VEHICLE_POSITIONS {
        override fun generateOutput(fm: FeedMessage) {
            val schema = CsvSchema.builder()
                    .setUseHeader(true)
                    .addColumn("feedEntity.id")
                    .addColumn("feedEntity.deleted")
                    .addColumn("trip.tripId")
                    .addColumn("trip.routeId")
                    .addColumn("trip.directionId")
                    .addColumn("trip.startDate")
                    .addColumn("trip.startTime")
                    .addColumn("trip.scheduleRelationship")
                    .addColumn("vehicle.id")
                    .addColumn("vehicle.label")
                    .addColumn("vehicle.licensePlate")
                    .addColumn("position.latitude")
                    .addColumn("position.longitude")
                    .addColumn("position.bearing")
                    .addColumn("position.odometer")
                    .addColumn("position.speed")
                    .addColumn("vehiclePosition.currentStatus")
                    .addColumn("vehiclePosition.stopId")
                    .addColumn("vehiclePosition.currentStopSequence")
                    .addColumn("vehiclePosition.timestamp")
                    .addColumn("vehiclePosition.congestionLevel")
                    .addColumn("vehiclePosition.occupancyStatus")
                    .build()

            val writer = CsvMapper().writerFor(Array<Any>::class.java).with(schema)
            val sw = writer.writeValues(System.out)

            fm.entityList
                    .filter(FeedEntity::hasVehicle)
                    .map { fe ->
                        val vp = fe.vehicle
                        val trip = vp.trip
                        val vehicle = vp.vehicle

                        arrayOf(
                                fe.id,
                                fe.isDeleted,
                                trip.tripId,
                                trip.routeId,
                                trip.directionId,
                                trip.startDate,
                                trip.startTime,
                                trip.scheduleRelationship,
                                vehicle.id,
                                vehicle.label,
                                vehicle.licensePlate,
                                vp.position.latitude,
                                vp.position.longitude,
                                vp.position.bearing,
                                vp.position.odometer,
                                vp.position.speed,
                                vp.currentStatus,
                                vp.stopId,
                                vp.currentStopSequence,
                                vp.timestamp,
                                vp.congestionLevel,
                                vp.occupancyStatus
                        )
                    }
                    .forEach { sw.write(it) }

        }
    },
    ALERTS {
        override fun generateOutput(fm: FeedMessage) {
            val schema = CsvSchema.builder()
                    .setUseHeader(true)
                    .addColumn("feedEntity.id")
                    .addColumn("feedEntity.deleted")
                    .addColumn("timeRange.start")
                    .addColumn("timeRange.end")
                    .addColumn("informedEntity.agencyId")
                    .addColumn("informedEntity.routeId")
                    .addColumn("informedEntity.routeType")
                    .addColumn("informedEntity.stopId")
                    .addColumn("informedEntity.trip.tripId")
                    .addColumn("informedEntity.trip.routeId")
                    .addColumn("informedEntity.trip.directionId")
                    .addColumn("informedEntity.trip.startDate")
                    .addColumn("informedEntity.trip.startTime")
                    .addColumn("informedEntity.trip.scheduleRelationship")
                    .addColumn("alert.cause")
                    .addColumn("alert.effect")
                    .addColumn("language")
                    .addColumn("url")
                    .addColumn("header")
                    .addColumn("description")
                    .build()

            val writer = CsvMapper().writerFor(Array<Any>::class.java).with(schema)
            val sw = writer.writeValues(System.out)

            fm.entityList
                    .filter(FeedEntity::hasAlert)
                    .flatMap { fe ->
                        val alert = fe.alert

                        val (urlsMap, headersMap, descriptionMap) = alertContentsByLanguage(alert)

                        val allLanguages = urlsMap.keys union headersMap.keys union descriptionMap.keys

                        (if (alert.activePeriodList.isNotEmpty())
                            alert.activePeriodList
                        else listOf(TimeRange.getDefaultInstance())).flatMap { ap ->
                            alert.informedEntityList.flatMap { ie ->
                                allLanguages.map { language ->
                                    arrayOf(
                                            fe.id,
                                            fe.isDeleted,
                                            ap.start,
                                            ap.end,
                                            ie.agencyId,
                                            ie.routeId,
                                            ie.routeType,
                                            ie.stopId,
                                            ie.trip.tripId,
                                            ie.trip.routeId,
                                            ie.trip.directionId,
                                            ie.trip.startDate,
                                            ie.trip.startTime,
                                            ie.trip.scheduleRelationship,
                                            alert.cause,
                                            alert.effect,
                                            language,
                                            urlsMap[language],
                                            headersMap[language],
                                            descriptionMap[language]
                                    )
                                }
                            }
                        }
                    }
                    .forEach { sw.write(it) }
        }
    }
}