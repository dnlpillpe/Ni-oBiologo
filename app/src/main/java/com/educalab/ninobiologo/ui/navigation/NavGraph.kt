package com.educalab.ninobiologo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.educalab.ninobiologo.AppContainer
import com.educalab.ninobiologo.domain.model.MicroscopeDiscovery
import com.educalab.ninobiologo.ui.screens.analyzer.AnalyzerScreen
import com.educalab.ninobiologo.ui.screens.celljourney.CellJourneyScreen
import com.educalab.ninobiologo.ui.screens.creature.CreatureBuilderScreen
import com.educalab.ninobiologo.ui.screens.environment.EnvironmentDetailScreen
import com.educalab.ninobiologo.ui.screens.experiment.ExperimentScreen
import com.educalab.ninobiologo.ui.screens.home.LaboratoryScreen
import com.educalab.ninobiologo.ui.screens.journal.JournalScreen
import com.educalab.ninobiologo.ui.screens.microscope.MicroscopeScreen
import com.educalab.ninobiologo.ui.screens.museum.DiscoveryDetailScreen
import com.educalab.ninobiologo.ui.screens.museum.MuseumScreen
import com.educalab.ninobiologo.ui.screens.onboarding.OnboardingScreen
import com.educalab.ninobiologo.ui.screens.profile.ProfileScreen
import com.educalab.ninobiologo.ui.screens.sample.SampleExplorationScreen
import com.educalab.ninobiologo.ui.viewmodel.AnalyzerViewModel
import com.educalab.ninobiologo.ui.viewmodel.CellJourneyViewModel
import com.educalab.ninobiologo.ui.viewmodel.CreatureBuilderViewModel
import com.educalab.ninobiologo.ui.viewmodel.EnvironmentViewModel
import com.educalab.ninobiologo.ui.viewmodel.ExperimentViewModel
import com.educalab.ninobiologo.ui.viewmodel.JournalViewModel
import com.educalab.ninobiologo.ui.viewmodel.LaboratoryViewModel
import com.educalab.ninobiologo.ui.viewmodel.MicroscopeViewModel
import com.educalab.ninobiologo.ui.viewmodel.MuseumViewModel
import com.educalab.ninobiologo.ui.viewmodel.NinoBiologoViewModelFactory
import com.educalab.ninobiologo.ui.viewmodel.OnboardingViewModel
import com.educalab.ninobiologo.ui.viewmodel.ProfileViewModel
import com.educalab.ninobiologo.ui.viewmodel.SampleExplorationViewModel

@Composable
fun NinoBiologoNavGraph(container: AppContainer) {
    val navController = rememberNavController()
    val factory = remember { NinoBiologoViewModelFactory(container) }

    val profileState by container.repository.observeProfile().collectAsState(initial = null)
    var startDestinationResolved by remember { mutableStateOf(false) }
    var startDestination by remember { mutableStateOf(Destinations.LABORATORY) }

    LaunchedEffect(profileState) {
        val profile = profileState
        if (profile != null) {
            startDestination = if (profile.onboardingCompleted) Destinations.LABORATORY else Destinations.ONBOARDING
            startDestinationResolved = true
        }
    }

    if (!startDestinationResolved) return

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.ONBOARDING) {
            val vm: OnboardingViewModel = viewModel(factory = factory)
            OnboardingScreen(viewModel = vm, onFinished = {
                navController.navigate(Destinations.LABORATORY) {
                    popUpTo(Destinations.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Destinations.LABORATORY) {
            val vm: LaboratoryViewModel = viewModel(factory = factory)
            LaboratoryScreen(
                viewModel = vm,
                onEnvironmentClick = { environmentId -> navController.navigate(Destinations.environmentRoute(environmentId)) },
                onMicroscopeClick = { navController.navigate(Destinations.MICROSCOPE) },
                onCellJourneyClick = { navController.navigate(Destinations.CELL_JOURNEY) },
                onMuseumClick = { navController.navigate(Destinations.MUSEUM) },
                onJournalClick = { navController.navigate(Destinations.JOURNAL) },
                onProfileClick = { navController.navigate(Destinations.PROFILE) }
            )
        }
        composable(
            Destinations.ENVIRONMENT,
            arguments = listOf(navArgument("environmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val environmentId = backStackEntry.arguments?.getString("environmentId") ?: return@composable
            val vm: EnvironmentViewModel = viewModel(factory = factory)
            EnvironmentDetailScreen(
                environmentId = environmentId,
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onSampleClick = { id -> navController.navigate(Destinations.sampleRoute(id)) },
                onExperimentClick = { id -> navController.navigate(Destinations.experimentRoute(id)) },
                onAnalyzerClick = { id -> navController.navigate(Destinations.analyzerRoute(id)) },
                onCreatureBuilderClick = { id -> navController.navigate(Destinations.creatureBuilderRoute(id)) }
            )
        }
        composable(
            Destinations.SAMPLE,
            arguments = listOf(navArgument("sampleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val sampleId = backStackEntry.arguments?.getString("sampleId") ?: return@composable
            val vm: SampleExplorationViewModel = viewModel(factory = factory)
            SampleExplorationScreen(sampleId = sampleId, viewModel = vm, onFinished = { navController.popBackStack() })
        }
        composable(Destinations.MICROSCOPE) {
            val vm: MicroscopeViewModel = viewModel(factory = factory)
            MicroscopeScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Destinations.CELL_JOURNEY) {
            val vm: CellJourneyViewModel = viewModel(factory = factory)
            CellJourneyScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Destinations.MUSEUM) {
            val vm: MuseumViewModel = viewModel(factory = factory)
            var selectedDiscovery by remember { mutableStateOf<MicroscopeDiscovery?>(null) }
            val current = selectedDiscovery
            if (current == null) {
                MuseumScreen(viewModel = vm, onDiscoveryClick = { selectedDiscovery = it }, onBack = { navController.popBackStack() })
            } else {
                DiscoveryDetailScreen(discovery = current, onBack = { selectedDiscovery = null })
            }
        }
        composable(
            Destinations.EXPERIMENT,
            arguments = listOf(navArgument("experimentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val experimentId = backStackEntry.arguments?.getString("experimentId") ?: return@composable
            val vm: ExperimentViewModel = viewModel(factory = factory)
            ExperimentScreen(experimentId = experimentId, viewModel = vm, onSaved = { navController.popBackStack() })
        }
        composable(
            Destinations.CREATURE_BUILDER,
            arguments = listOf(navArgument("environmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val environmentId = backStackEntry.arguments?.getString("environmentId") ?: return@composable
            val vm: CreatureBuilderViewModel = viewModel(factory = factory)
            CreatureBuilderScreen(environmentId = environmentId, viewModel = vm, onSaved = { navController.popBackStack() })
        }
        composable(
            Destinations.ANALYZER,
            arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getString("challengeId") ?: return@composable
            val vm: AnalyzerViewModel = viewModel(factory = factory)
            AnalyzerScreen(challengeId = challengeId, viewModel = vm, onFinished = { navController.popBackStack() })
        }
        composable(Destinations.JOURNAL) {
            val vm = viewModel<JournalViewModel>(factory = factory)
            JournalScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Destinations.PROFILE) {
            val vm: ProfileViewModel = viewModel(factory = factory)
            ProfileScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
    }
}
