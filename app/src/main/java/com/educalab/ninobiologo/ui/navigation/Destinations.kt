package com.educalab.ninobiologo.ui.navigation

object Destinations {
    const val ONBOARDING = "onboarding"
    const val EXPEDITION_MAP = "expedition_map"
    const val ZONE = "zone/{biomeId}"
    const val EXPEDITION = "expedition/{expeditionId}"
    const val MICROSCOPE = "microscope"
    const val MUSEUM = "museum"
    const val ECOSYSTEM_BUILDER = "ecosystem_builder/{templateId}"
    const val CLASSIFIER = "classifier/{challengeId}"
    const val JOURNAL = "journal"
    const val PROFILE = "profile"

    fun zoneRoute(biomeId: String) = "zone/$biomeId"
    fun expeditionRoute(expeditionId: String) = "expedition/$expeditionId"
    fun ecosystemBuilderRoute(templateId: String) = "ecosystem_builder/$templateId"
    fun classifierRoute(challengeId: String) = "classifier/$challengeId"
}
