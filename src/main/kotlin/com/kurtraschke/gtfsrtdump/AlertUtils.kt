package com.kurtraschke.gtfsrtdump

import com.google.transit.realtime.GtfsRealtime

data class AlertContents(val urlsByLanguage: Map<String, String>,
                         val headersByLanguage: Map<String, String>,
                         val descriptionsByLanguage: Map<String, String>)

fun alertContentsByLanguage(alert: GtfsRealtime.Alert): AlertContents {
    val urlsMap = alert.url.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" },
            valueTransform = GtfsRealtime.TranslatedString.Translation::getText
    )

    val headersMap = alert.headerText.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" },
            valueTransform = GtfsRealtime.TranslatedString.Translation::getText
    )

    val descriptionMap = alert.descriptionText.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" },
            valueTransform = GtfsRealtime.TranslatedString.Translation::getText
    )

    return AlertContents(urlsMap, headersMap, descriptionMap)
}