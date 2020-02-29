package com.kurtraschke.gtfsrtdump.output;

import com.google.transit.realtime.GtfsRealtime;
import com.kurtraschke.gtfsrtdump.TimestampFormatting;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import static com.kurtraschke.gtfsrtdump.output.table.FeedEntityFormatter.formatFeedEntities;
import static com.kurtraschke.gtfsrtdump.output.table.FeedHeaderFormatter.formatFeedHeader;

@Command(name = "table", description = "Formatted table output")
public class TableOutput extends OutputMethod {

    @Option(names = {"--timestamp-format"},
            defaultValue = "ISO_8601_LOCAL",
            description = "Valid values: ${COMPLETION-CANDIDATES}")
    TimestampFormatting timestampFormatter;

    @Override
    public Integer format(GtfsRealtime.FeedMessage fm) {
        System.out.print(formatFeedHeader(fm.getHeader(), timestampFormatter));
        System.out.print(formatFeedEntities(fm.getEntityList(), timestampFormatter));

        return 0;
    }
}
