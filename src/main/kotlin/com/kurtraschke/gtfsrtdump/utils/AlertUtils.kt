package com.kurtraschke.gtfsrtdump.utils

import com.google.transit.realtime.GtfsRealtime.Alert
import com.google.transit.realtime.GtfsRealtime.TranslatedString.Translation

data class AlertContents(
    val urlsByLanguage: Map<String, String>,
    val headersByLanguage: Map<String, String>,
    val descriptionsByLanguage: Map<String, String>
) {
    val languages: Set<String>
        get() {
            return urlsByLanguage.keys union headersByLanguage.keys union descriptionsByLanguage.keys
        }

    constructor(alert: Alert) : this(
        alert.url.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" }, valueTransform = Translation::getText
        ), alert.headerText.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" }, valueTransform = Translation::getText
        ), alert.descriptionText.translationList.associateBy(
            keySelector = { if (it.hasLanguage()) it.language else "" }, valueTransform = Translation::getText
        )
    )
}
