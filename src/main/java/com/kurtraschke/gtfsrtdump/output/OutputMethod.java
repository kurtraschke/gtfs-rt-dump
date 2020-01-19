package com.kurtraschke.gtfsrtdump.output;

import com.google.transit.realtime.GtfsRealtime;
import com.kurtraschke.gtfsrtdump.Main;
import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;

import java.util.concurrent.Callable;

@Command
abstract class OutputMethod implements Callable<Integer> {
    @ParentCommand
    private Main main;

    @Override
    public Integer call() throws Exception {
        final GtfsRealtime.FeedMessage fm = main.getFeedMessage();

        return format(fm);
    }

    abstract Integer format(GtfsRealtime.FeedMessage fm) throws Exception;
}
