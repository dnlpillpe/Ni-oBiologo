package com.educalab.ninobiologo.ui.navigation

object Destinations {
    const val ONBOARDING = "onboarding"
    const val LABORATORY = "laboratory"
    const val ENVIRONMENT = "environment/{environmentId}"
    const val SAMPLE = "sample/{sampleId}"
    const val MICROSCOPE = "microscope"
    const val CELL_JOURNEY = "cell_journey"
    const val MUSEUM = "museum"
    const val EXPERIMENT = "experiment/{experimentId}"
    const val CREATURE_BUILDER = "creature_builder/{environmentId}"
    const val ANALYZER = "analyzer/{challengeId}"
    const val JOURNAL = "journal"
    const val PROFILE = "profile"

    fun environmentRoute(environmentId: String) = "environment/$environmentId"
    fun sampleRoute(sampleId: String) = "sample/$sampleId"
    fun experimentRoute(experimentId: String) = "experiment/$experimentId"
    fun creatureBuilderRoute(environmentId: String) = "creature_builder/$environmentId"
    fun analyzerRoute(challengeId: String) = "analyzer/$challengeId"
}
