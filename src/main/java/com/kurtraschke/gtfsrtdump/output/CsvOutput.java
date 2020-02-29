package com.kurtraschke.gtfsrtdump.output;

import com.google.transit.realtime.GtfsRealtime;
import com.kurtraschke.gtfsrtdump.output.csv.FeedParts;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "csv", description = "CSV output")
public class CsvOutput extends OutputMethod {

    @Parameters(index = "0", description = "Feed part to extract to CSV. Valid values: ${COMPLETION-CANDIDATES}")
    FeedParts selectedFeedPart;

    @Override
    public Integer format(GtfsRealtime.FeedMessage fm) {
        selectedFeedPart.generateOutput(fm);
        return 0;
    }
}
