package com.kurtraschke.gtfsrtdump.output.csv

import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.output.OutputMethod
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import java.io.PrintWriter

@Command(
    name = "csv", description = ["CSV output"]
)
class CsvOutput : OutputMethod() {

    @Parameters(
        index = "0", description = ["Feed part to extract to CSV. Valid values: \${COMPLETION-CANDIDATES}"]
    )
    private lateinit var selectedFeedPart: FeedParts

    override fun format(fm: FeedMessage, w: PrintWriter): Int {
        selectedFeedPart.generateOutput(fm, w)
        return 0
    }
}
