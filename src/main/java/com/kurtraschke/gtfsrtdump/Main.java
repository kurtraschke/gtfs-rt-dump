/*
 * Copyright (C) 2014 Kurt Raschke <kurt@kurtraschke.com>
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
import com.google.transit.realtime.GtfsRealtime;
import com.google.transit.realtime.GtfsRealtimeNYCT;
import com.google.transit.realtime.GtfsRealtimeOneBusAway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 *
 * @author kurt
 */
public class Main {

  private static final Logger _log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    ArgumentParser parser = ArgumentParsers.newArgumentParser("gtfs-rt-dump")
            .defaultHelp(true);

    MutuallyExclusiveGroup group = parser.addMutuallyExclusiveGroup().required(true);

    group.addArgument("-u", "--url")
            .type(URL.class)
            .help("URL containing GTFS-realtime feed to parse and display");

    group.addArgument("-f", "--file")
            .type(Arguments.fileType().acceptSystemIn().verifyCanRead())
            .setDefault("-")
            .help("File containing GTFS-realtime feed to parse and display");

    try {
      Namespace parseArgs = parser.parseArgs(args);

      Object f = parseArgs.get("file");
      Object u = parseArgs.get("url");

      InputStream is;

      if (u != null) {
        is = ((URL) u).openStream();
      } else {
        if (((File) f).getPath().equals("-")) {
          is = System.in;
        } else {
          is = new FileInputStream((File) f);
        }
      }

      ExtensionRegistry registry = ExtensionRegistry.newInstance();

      registry.add(GtfsRealtimeOneBusAway.obaFeedEntity);
      registry.add(GtfsRealtimeOneBusAway.obaTripUpdate);
      registry.add(GtfsRealtimeNYCT.nyctFeedHeader);
      registry.add(GtfsRealtimeNYCT.nyctStopTimeUpdate);
      registry.add(GtfsRealtimeNYCT.nyctTripDescriptor);

      System.out.println(GtfsRealtime.FeedMessage.parseFrom(is, registry).toString());

      is.close();
    } catch (ArgumentParserException e) {
      parser.handleError(e);
    } catch (IOException ex) {
      _log.error("IO exception", ex);
    }
  }
}
