package com.kurtraschke.gtfsrtdump.output.table

import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.output.OutputMethod
import com.kurtraschke.gtfsrtdump.utils.TimestampFormatting
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.PrintWriter

@Command(
    name = "table", description = ["Formatted table output"]
)
class TableOutput : OutputMethod() {

    @Option(
        names = ["--timestamp-format"],
        defaultValue = "ISO_8601_LOCAL",
        description = ["Valid values: \${COMPLETION-CANDIDATES}"]
    )
    lateinit var timestampFormatter: TimestampFormatting

    override fun format(fm: FeedMessage, w: PrintWriter): Int {
        w.println(formatFeedHeader(fm.header, timestampFormatter))
        w.print(formatFeedEntities(fm.entityList, timestampFormatter))
        return 0
    }
}
