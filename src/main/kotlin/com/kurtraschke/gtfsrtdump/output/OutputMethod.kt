package com.kurtraschke.gtfsrtdump.output

import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.Main
import picocli.CommandLine.*
import java.util.concurrent.Callable

@Command
abstract class OutputMethod : Callable<Int> {
    @ParentCommand
    private lateinit var main: Main

    override fun call(): Int {
        val fm = main.feedMessage
        return format(fm)
    }

    abstract fun format(fm: FeedMessage): Int
}