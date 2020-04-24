package com.kurtraschke.gtfsrtdump.output;

import com.google.transit.realtime.GtfsRealtime.FeedMessage;
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
        final FeedMessage fm = main.getFeedMessage();

        return format(fm);
    }

    abstract Integer format(FeedMessage fm) throws Exception;
}
