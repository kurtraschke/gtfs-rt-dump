package com.kurtraschke.gtfsrtdump.output

import com.google.protobuf.TextFormat
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.TimestampFormatter
import com.kurtraschke.gtfsrtdump.TimestampFormatting
import picocli.CommandLine.Command
import picocli.CommandLine.Option

private val TIMESTAMP_PATTERN = Regex("(?:start|end|time(?:stamp)?): (\\d{10})")

private fun enhanceMessageOutput(formattedMessage: String, tf: TimestampFormatter): String {
    return TIMESTAMP_PATTERN.replace(formattedMessage) { matchResult: MatchResult ->
        "${matchResult.value}\t/* ${tf.formatTimestamp(matchResult.groups[1]!!.value.toLong())} */"
    }
}

@Command(name = "pbtext",
        description = ["Protocol Buffer text format output"])
class PbTextOutput : OutputMethod() {

    @Option(names = ["--timestamp-format"],
            arity = "0..1",
            fallbackValue = "ISO_8601_LOCAL",
            description = ["Valid values: \${COMPLETION-CANDIDATES}"])
    private var timestampFormatter: TimestampFormatting? = null

    override fun format(fm: FeedMessage): Int {
        var formattedMessage = TextFormat.printToString(fm)

        timestampFormatter?.let {
            formattedMessage = enhanceMessageOutput(formattedMessage, it)
        }

        print(formattedMessage)

        return 0
    }
}