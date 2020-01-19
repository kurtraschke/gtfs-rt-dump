package com.kurtraschke.gtfsrtdump.output;

import com.google.protobuf.TextFormat;
import com.google.transit.realtime.GtfsRealtime;
import picocli.CommandLine;

@CommandLine.Command(name = "pbtext", description = "Protocol Buffer text format output")
public
class PbTextOutput extends OutputMethod {

    @Override
    public Integer format(GtfsRealtime.FeedMessage fm) throws Exception {
        TextFormat.print(fm, System.out);

        return 0;
    }
}
