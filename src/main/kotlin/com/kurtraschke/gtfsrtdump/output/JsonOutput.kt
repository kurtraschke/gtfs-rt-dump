package com.kurtraschke.gtfsrtdump.output

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.hubspot.jackson.datatype.protobuf.ProtobufJacksonConfig
import com.hubspot.jackson.datatype.protobuf.ProtobufModule
import picocli.CommandLine.Command
import java.io.PrintWriter

@Command(name = "json", description = ["JSON output"])
class JsonOutput : OutputMethod() {

    override fun format(fm: FeedMessage, w: PrintWriter): Int {
        val config = ProtobufJacksonConfig.builder().extensionRegistry(main.registry).build()

        val mapper = ObjectMapper()
        mapper.registerModule(ProtobufModule(config))
        mapper.writerWithDefaultPrettyPrinter().writeValue(w, fm)
        return 0
    }

}
