package com.kurtraschke.gtfsrtdump

import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErr
import com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import com.github.tomakehurst.wiremock.junit5.WireMockExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import picocli.CommandLine
import java.nio.file.Paths
import kotlin.test.assertEquals

class DumpIT {

    @Test
    fun testDump() {
        val testInput = Paths.get("src", "test", "resources", "TripUpdates.pb")

        val errText = tapSystemErr {
            val outText = tapSystemOutNormalized {
                val cmd = CommandLine(Main()).setCaseInsensitiveEnumValuesAllowed(true)

                val exitCode = cmd.execute("-f", testInput.toAbsolutePath().toString(), "csv", "FEED_HEADER")

                assertEquals(0, exitCode)
            }
            assertEquals(
                """
                    version,timestamp,incrementality
                    2.0,1686935289,FULL_DATASET
                    
                    """.trimIndent(),
                outText
            )
        }
    }

    companion object {
        @Suppress("unused")
        @JvmField
        @RegisterExtension
        var wm: WireMockExtension = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8080))
            .proxyMode(true)
            .build()
    }
}