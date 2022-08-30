package com.kurtraschke.gtfsrtdump

import com.google.protobuf.ExtensionRegistry
import com.google.transit.realtime.*

internal interface GtfsRealtimeExtension {
    fun registerExtension(registry: ExtensionRegistry)
}

@Suppress("unused")
enum class GtfsRealtimeExtensions : GtfsRealtimeExtension {
    OBA {
        override fun registerExtension(registry: ExtensionRegistry) {
            GtfsRealtimeOneBusAway.registerAllExtensions(registry)
        }
    },
    NYCT {
        override fun registerExtension(registry: ExtensionRegistry) {
            GtfsRealtimeNYCT.registerAllExtensions(registry)
        }
    },
    LIRR {
        override fun registerExtension(registry: ExtensionRegistry) {
            GtfsRealtimeLIRR.registerAllExtensions(registry)
        }
    },
    MNR {
        override fun registerExtension(registry: ExtensionRegistry) {
            GtfsRealtimeMNR.registerAllExtensions(registry)
        }
    },
    MTARR {
        override fun registerExtension(registry: ExtensionRegistry) {
            GtfsRealtimeMTARR.registerAllExtensions(registry)
        }
    },
    MERCURY {
        override fun registerExtension(registry: ExtensionRegistry) {
            GtfsRealtimeServiceStatus.registerAllExtensions(registry)
        }
    }
}
