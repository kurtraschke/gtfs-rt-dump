/*
 * Copyright (C) 2022 Kurt Raschke <kurt@kurtraschke.com>
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
package com.kurtraschke.gtfsrtdump

import com.google.common.io.CountingInputStream
import com.google.protobuf.ExtensionRegistry
import com.google.transit.realtime.GtfsRealtime.FeedMessage
import com.kurtraschke.gtfsrtdump.output.JsonOutput
import com.kurtraschke.gtfsrtdump.output.PbTextOutput
import com.kurtraschke.gtfsrtdump.output.csv.CsvOutput
import com.kurtraschke.gtfsrtdump.output.table.TableOutput
import com.kurtraschke.gtfsrtdump.utils.NullX509ExtendedTrustManager
import picocli.CommandLine
import picocli.CommandLine.*
import picocli.CommandLine.Model.CommandSpec
import java.io.InputStream
import java.io.PrintWriter
import java.net.Authenticator
import java.net.PasswordAuthentication
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Callable
import java.util.logging.Logger
import javax.net.ssl.SSLContext
import kotlin.system.exitProcess

private val LOG = Logger.getLogger("com.kurtraschke.gtfsrtdump")

@Suppress("UnstableApiUsage")
@Command(
    name = "gtfs-rt-dump",
    description = ["Parse and display the contents of a GTFS-realtime feed in a human-readable format."],
    mixinStandardHelpOptions = true,
    version = ["1.1"],
    synopsisSubcommandLabel = "COMMAND",
    subcommands = [HelpCommand::class, PbTextOutput::class, TableOutput::class, JsonOutput::class, CsvOutput::class]
)
class Main : Callable<Int> {

    private class ProtobufSource {
        @Option(
            names = ["-f", "--file"], description = ["Read GTFS-rt from specified file."], required = true
        )
        var protobufPath: Path? = null

        @Option(
            names = ["-u", "--url"], description = ["Read GTFS-rt from specified URL."], required = true
        )
        var protobufUrl: URL? = null
    }

    private class BasicAuthenticationCredential {
        @Option(
            names = ["-U", "--username"], description = ["Username for HTTP Basic authentication"], required = true
        )
        var username: String? = null

        @Option(
            names = ["-P", "--password"], description = ["Password for HTTP Basic authentication"], required = true
        )
        var password: String? = null
    }

    @ArgGroup(heading = "Input can be read from a file or URL. If neither are specified, standard input will be used.%n")
    private var protobufSource: ProtobufSource? = null

    @ArgGroup(
        heading = "HTTP Basic authentication credentials can be specified if input is read from a URL.%n",
        exclusive = false
    )
    private var basicAuthenticationCredentials: BasicAuthenticationCredential? = null

    @Option(
        names = ["-H", "--header"], description = ["Add specified HTTP header to request."]
    )
    private var headers: Map<String, String> = emptyMap()

    @Option(
        names = ["--disable-tls-validation"], description = ["Disable all TLS server certificate validation."]
    )
    private var disableTlsValidation = false

    @Option(
        names = ["-X", "--enable-extension"],
        description = ["Enable specified GTFS-rt extensions. Valid values: \${COMPLETION-CANDIDATES}"]
    )
    private var enabledExtensions: List<GtfsRealtimeExtensions> = emptyList()

    @Spec
    private lateinit var spec: CommandSpec

    val registry: ExtensionRegistry by lazy {
        val r = ExtensionRegistry.newInstance()
        enabledExtensions.forEach { it.registerExtension(r) }

        return@lazy r
    }

    val out: PrintWriter
        get() {
            return spec.commandLine().out
        }

    val err: PrintWriter
        get() {
            return spec.commandLine().err
        }

    val feedMessage: FeedMessage by lazy {
        val inputStream: InputStream = when {
            protobufSource?.protobufUrl != null -> {
                val inputUrl = protobufSource!!.protobufUrl!!

                val clientBuilder = HttpClient.newBuilder()

                basicAuthenticationCredentials?.let {
                    clientBuilder.authenticator(object : Authenticator() {
                        override fun getPasswordAuthentication(): PasswordAuthentication {
                            return PasswordAuthentication(
                                it.username, it.password?.toCharArray()
                            )
                        }
                    })
                }

                if (disableTlsValidation) {
                    LOG.warning("TLS server certificate validation is disabled.")
                    val context = SSLContext.getInstance("TLS")
                    context.init(
                        null, arrayOf(NullX509ExtendedTrustManager()), null
                    )
                    clientBuilder.sslContext(context)
                }

                val client = clientBuilder.build()
                val builder = HttpRequest.newBuilder(inputUrl.toURI())

                if (headers.isNotEmpty()) {
                    builder.headers(*headers.entries.flatMap { listOf(it.key, it.value) }.toTypedArray())
                }

                val response = client.send(builder.build(), BodyHandlers.ofInputStream())

                response.body()
            }
            protobufSource?.protobufPath != null -> {
                val inputPath = protobufSource!!.protobufPath!!
                Files.newInputStream(inputPath)
            }
            else -> System.`in`
        }

        val cis = CountingInputStream(inputStream)

        val fm = FeedMessage.parseFrom(cis, registry)

        inputStream.close()

        LOG.info { String.format("Read %d bytes from source.", cis.count) }

        return@lazy fm
    }

    override fun call(): Int {
        throw ParameterException(spec.commandLine(), "Output format must be specified.")
    }
}

fun main(args: Array<String>) {
    exitProcess(
        CommandLine(Main()).setCaseInsensitiveEnumValuesAllowed(true).execute(*args)
    )
}
