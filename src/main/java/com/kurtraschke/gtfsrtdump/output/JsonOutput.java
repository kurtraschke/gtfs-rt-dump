package com.kurtraschke.gtfsrtdump.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.transit.realtime.GtfsRealtime;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import picocli.CommandLine.Command;

@Command(name = "json", description = "JSON output")
public
class JsonOutput extends OutputMethod {
    @Override
    public Integer format(GtfsRealtime.FeedMessage fm) throws Exception {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new ProtobufModule());

        mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, fm);

        return 0;
    }
}
