/*
 * Copyright (C) 2020 Kurt Raschke <kurt@kurtraschke.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.kurtraschke.gtfsrtdump;

import com.google.protobuf.ExtensionRegistry;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtimeNYCT;
import com.google.transit.realtime.GtfsRealtimeOneBusAway;
import com.kurtraschke.gtfsrtdump.output.CsvOutput;
import com.kurtraschke.gtfsrtdump.output.JsonOutput;
import com.kurtraschke.gtfsrtdump.output.PbTextOutput;
import com.kurtraschke.gtfsrtdump.output.TableOutput;
import picocli.CommandLine;
import picocli.CommandLine.*;
import picocli.CommandLine.Model.CommandSpec;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import static java.net.http.HttpResponse.BodyHandlers.ofInputStream;

@Command(name = "gtfs-rt-dump",
        description = "Parse and display the contents of a GTFS-realtime feed in a human-readable format.",
        mixinStandardHelpOptions = true,
        version = "1.1",
        synopsisSubcommandLabel = "COMMAND",
        subcommands = {
                PbTextOutput.class,
                TableOutput.class,
                JsonOutput.class,
                CsvOutput.class
        })
public class Main implements Callable<Integer> {
    @ArgGroup(heading = "Input can be read from a file or URL. If neither are specified, standard input will be used.%n")
    ProtobufSource protobufSource;

    static class ProtobufSource {
        @Option(names = {"-f", "--file"},
                description = "Read GTFS-rt from specified file.",
                required = true)
        Path protobufPath;

        @Option(names = {"-u", "--url"},
                description = "Read GTFS-rt from specified URL.",
                required = true)
        URL protobufUrl;
    }

    @Option(names = {"-H", "--header"}, description = "Add specified HTTP header to request.")
    Map<String, String> headers;

    @Spec
    CommandSpec spec;

    public FeedMessage getFeedMessage() throws IOException, InterruptedException, URISyntaxException {
        final InputStream is;

        if (protobufSource != null && protobufSource.protobufUrl != null) {
            final URL inputUrl = protobufSource.protobufUrl;

            final HttpClient client = HttpClient.newHttpClient();

            final HttpRequest.Builder builder = HttpRequest.newBuilder(inputUrl.toURI());

            if (headers != null && !headers.isEmpty()) {
                builder.headers(
                        headers.entrySet().stream()
                                .flatMap(e -> Stream.of(e.getKey(), e.getValue()))
                                .toArray(String[]::new)
                );
            }

            final HttpResponse<InputStream> response = client.send(builder.build(), ofInputStream());

            is = response.body();

        } else if (protobufSource != null && protobufSource.protobufPath != null) {
            final Path inputPath = protobufSource.protobufPath;

            is = Files.newInputStream(inputPath);
        } else {
            is = System.in;
        }

        final ExtensionRegistry registry = ExtensionRegistry.newInstance();

        registry.add(GtfsRealtimeOneBusAway.obaFeedHeader);
        registry.add(GtfsRealtimeOneBusAway.obaFeedEntity);
        registry.add(GtfsRealtimeOneBusAway.obaTripUpdate);
        registry.add(GtfsRealtimeNYCT.nyctFeedHeader);
        registry.add(GtfsRealtimeNYCT.nyctStopTimeUpdate);
        registry.add(GtfsRealtimeNYCT.nyctTripDescriptor);

        final FeedMessage fm = FeedMessage.parseFrom(is, registry);

        is.close();

        return fm;
    }

    @Override
    public Integer call() throws Exception {
        throw new ParameterException(spec.commandLine(), "Output format must be specified.");
    }

    public static void main(String... args) {
        System.exit(new CommandLine(new Main())
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args));
    }
}

