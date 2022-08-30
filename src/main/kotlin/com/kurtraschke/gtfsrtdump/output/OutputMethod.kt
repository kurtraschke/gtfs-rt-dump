package com.kurtraschke.gtfsrtdump.output

import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.Main
import picocli.CommandLine.Command
import picocli.CommandLine.ParentCommand
import java.io.PrintWriter
import java.util.concurrent.Callable

@Command
abstract class OutputMethod : Callable<Int> {
    @ParentCommand
    lateinit var main: Main

    override fun call(): Int {
        val fm = main.feedMessage
        format(fm, main.out)
        main.out.flush()
        return 0
    }

    abstract fun format(fm: FeedMessage, w: PrintWriter): Int
}
