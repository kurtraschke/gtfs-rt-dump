package com.kurtraschke.gtfsrtdump.output

import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.utils.TimestampFormatter
import com.kurtraschke.gtfsrtdump.utils.TimestampFormatting
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.PrintWriter

private val TIMESTAMP_PATTERN = Regex("(?:start|end|time(?:stamp)?|(?:updated|created)_at): (\\d+)")

private fun enhanceMessageOutput(formattedMessage: String, tf: TimestampFormatter): String {
    return TIMESTAMP_PATTERN.replace(formattedMessage) { matchResult ->
        "${matchResult.value}\t/* ${tf.formatTimestamp(matchResult.groups[1]!!.value.toLong())} */"
    }
}

@Command(
    name = "pbtext", description = ["Protocol Buffer text format output"]
)
class PbTextOutput : OutputMethod() {

    @Option(
        names = ["--timestamp-format"],
        arity = "0..1",
        fallbackValue = "ISO_8601_LOCAL",
        description = ["Valid values: \${COMPLETION-CANDIDATES}"]
    )
    private var timestampFormatter: TimestampFormatting? = null

    override fun format(fm: FeedMessage, w: PrintWriter): Int {
        var formattedMessage = fm.toString()

        timestampFormatter?.let {
            formattedMessage = enhanceMessageOutput(formattedMessage, it)
        }

        w.print(formattedMessage)

        return 0
    }
}
