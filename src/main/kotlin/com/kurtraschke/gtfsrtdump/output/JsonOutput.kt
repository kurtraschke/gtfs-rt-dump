package com.kurtraschke.gtfsrtdump.output

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.hubspot.jackson.datatype.protobuf.ProtobufModule
import picocli.CommandLine.Command

@Command(name = "json", description = ["JSON output"])
class JsonOutput : OutputMethod() {

    override fun format(fm: FeedMessage): Int {
        val mapper = ObjectMapper()
        mapper.registerModule(ProtobufModule())
        mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, fm)
        return 0
    }

}