package com.kurtraschke.gtfsrtdump.output;

import com.google.protobuf.TextFormat;
import com.google.transit.realtime.GtfsRealtime;
import com.kurtraschke.gtfsrtdump.TimestampFormatting;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Command(name = "pbtext", description = "Protocol Buffer text format output")
public class PbTextOutput extends OutputMethod {
    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("(?:start|end|time(?:stamp)?): (\\d{10})");

    @Option(names = {"--timestamp-format"}, arity = "0..1",
            fallbackValue = "ISO_8601_LOCAL",
            description = "Valid values: ${COMPLETION-CANDIDATES}")
    TimestampFormatting timestampFormatter;

    @Override
    public Integer format(GtfsRealtime.FeedMessage fm) throws IOException {
        String formattedMessage = TextFormat.printToString(fm);

        if (timestampFormatter != null) {
            formattedMessage = enhanceMessageOutput(formattedMessage);
        }

        System.out.print(formattedMessage);

        return 0;
    }

    private String enhanceMessageOutput(String formattedMessage) {
        final Matcher matcher = TIMESTAMP_PATTERN.matcher(formattedMessage);

        final String enhancedMessage = matcher.replaceAll(
                matchResult -> String.format(
                        "%s\t/* %s */",
                        matchResult.group(),
                        timestampFormatter.formatTimestamp(Integer.parseInt(matchResult.group(1)))
                )
        );

        return enhancedMessage;
    }
}
