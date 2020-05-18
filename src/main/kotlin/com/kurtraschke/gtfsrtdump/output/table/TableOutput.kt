package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.TimestampFormatting
import com.kurtraschke.gtfsrtdump.output.OutputMethod
import picocli.CommandLine.Command
import picocli.CommandLine.Option

@Command(name = "table",
        description = ["Formatted table output"])
class TableOutput : OutputMethod() {

    @Option(names = ["--timestamp-format"],
            defaultValue = "ISO_8601_LOCAL",
            description = ["Valid values: \${COMPLETION-CANDIDATES}"])
    lateinit var timestampFormatter: TimestampFormatting

    override fun format(fm: FeedMessage): Int {
        print(formatFeedHeader(fm.header, timestampFormatter))
        print(formatFeedEntities(fm.entityList, timestampFormatter))
        return 0
    }
}