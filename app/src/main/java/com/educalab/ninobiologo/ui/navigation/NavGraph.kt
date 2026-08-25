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
import com.educalab.ninobiologo.domain.model.Organism
import com.educalab.ninobiologo.ui.screens.classifier.ClassifierScreen
import com.educalab.ninobiologo.ui.screens.ecosystem.EcosystemBuilderScreen
import com.educalab.ninobiologo.ui.screens.expedition.ExpeditionScreen
import com.educalab.ninobiologo.ui.screens.home.ExpeditionMapScreen
import com.educalab.ninobiologo.ui.screens.journal.JournalScreen
import com.educalab.ninobiologo.ui.screens.microscope.MicroscopeScreen
import com.educalab.ninobiologo.ui.screens.museum.MuseumScreen
import com.educalab.ninobiologo.ui.screens.museum.OrganismDetailScreen
import com.educalab.ninobiologo.ui.screens.onboarding.OnboardingScreen
import com.educalab.ninobiologo.ui.screens.profile.ProfileScreen
import com.educalab.ninobiologo.ui.screens.zone.ZoneDetailScreen
import com.educalab.ninobiologo.ui.viewmodel.ClassifierViewModel
import com.educalab.ninobiologo.ui.viewmodel.EcosystemBuilderViewModel
import com.educalab.ninobiologo.ui.viewmodel.ExpeditionMapViewModel
import com.educalab.ninobiologo.ui.viewmodel.ExpeditionViewModel
import com.educalab.ninobiologo.ui.viewmodel.MicroscopeViewModel
import com.educalab.ninobiologo.ui.viewmodel.MuseumViewModel
import com.educalab.ninobiologo.ui.viewmodel.NinoBiologoViewModelFactory
import com.educalab.ninobiologo.ui.viewmodel.OnboardingViewModel
import com.educalab.ninobiologo.ui.viewmodel.ProfileViewModel
import com.educalab.ninobiologo.ui.viewmodel.ZoneViewModel

@Composable
fun NinoBiologoNavGraph(container: AppContainer) {
    val navController = rememberNavController()
    val factory = remember { NinoBiologoViewModelFactory(container) }

    val profileState by container.repository.observeProfile().collectAsState(initial = null)
    var startDestinationResolved by remember { mutableStateOf(false) }
    var startDestination by remember { mutableStateOf(Destinations.EXPEDITION_MAP) }

    LaunchedEffect(profileState) {
        val profile = profileState
        if (profile != null) {
            startDestination = if (profile.onboardingCompleted) Destinations.EXPEDITION_MAP else Destinations.ONBOARDING
            startDestinationResolved = true
        }
    }

    if (!startDestinationResolved) return

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.ONBOARDING) {
            val vm: OnboardingViewModel = viewModel(factory = factory)
            OnboardingScreen(viewModel = vm, onFinished = {
                navController.navigate(Destinations.EXPEDITION_MAP) {
                    popUpTo(Destinations.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Destinations.EXPEDITION_MAP) {
            val vm: ExpeditionMapViewModel = viewModel(factory = factory)
            ExpeditionMapScreen(
                viewModel = vm,
                onZoneClick = { biomeId -> navController.navigate(Destinations.zoneRoute(biomeId)) },
                onProfileClick = { navController.navigate(Destinations.PROFILE) },
                onJournalClick = { navController.navigate(Destinations.JOURNAL) },
                onMuseumClick = { navController.navigate(Destinations.MUSEUM) }
            )
        }
        composable(
            Destinations.ZONE,
            arguments = listOf(navArgument("biomeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val biomeId = backStackEntry.arguments?.getString("biomeId") ?: return@composable
            val vm: ZoneViewModel = viewModel(factory = factory)
            ZoneDetailScreen(
                biomeId = biomeId,
                viewModel = vm,
                onBack = { navController.popBackStack() },
                onExpeditionClick = { id -> navController.navigate(Destinations.expeditionRoute(id)) },
                onEcosystemClick = { id -> navController.navigate(Destinations.ecosystemBuilderRoute(id)) },
                onChallengeClick = { id -> navController.navigate(Destinations.classifierRoute(id)) },
                onMicroscopeClick = { navController.navigate(Destinations.MICROSCOPE) }
            )
        }
        composable(
            Destinations.EXPEDITION,
            arguments = listOf(navArgument("expeditionId") { type = NavType.StringType })
        ) { backStackEntry ->
            val expeditionId = backStackEntry.arguments?.getString("expeditionId") ?: return@composable
            val vm: ExpeditionViewModel = viewModel(factory = factory)
            ExpeditionScreen(
                expeditionId = expeditionId,
                viewModel = vm,
                onFinished = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destinations.MICROSCOPE) {
            val vm: MicroscopeViewModel = viewModel(factory = factory)
            MicroscopeScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Destinations.MUSEUM) {
            val vm: MuseumViewModel = viewModel(factory = factory)
            var selectedOrganism by remember { mutableStateOf<Organism?>(null) }
            val current = selectedOrganism
            if (current == null) {
                MuseumScreen(viewModel = vm, onOrganismClick = { selectedOrganism = it }, onBack = { navController.popBackStack() })
            } else {
                OrganismDetailScreen(organism = current, onBack = { selectedOrganism = null })
            }
        }
        composable(
            Destinations.ECOSYSTEM_BUILDER,
            arguments = listOf(navArgument("templateId") { type = NavType.StringType })
        ) { backStackEntry ->
            val templateId = backStackEntry.arguments?.getString("templateId") ?: return@composable
            val vm: EcosystemBuilderViewModel = viewModel(factory = factory)
            EcosystemBuilderScreen(templateId = templateId, viewModel = vm, onSaved = { navController.popBackStack() })
        }
        composable(
            Destinations.CLASSIFIER,
            arguments = listOf(navArgument("challengeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getString("challengeId") ?: return@composable
            val vm: ClassifierViewModel = viewModel(factory = factory)
            ClassifierScreen(challengeId = challengeId, viewModel = vm, onFinished = { navController.popBackStack() })
        }
        composable(Destinations.JOURNAL) {
            val vm = viewModel<com.educalab.ninobiologo.ui.viewmodel.JournalViewModel>(factory = factory)
            JournalScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
        composable(Destinations.PROFILE) {
            val vm: ProfileViewModel = viewModel(factory = factory)
            ProfileScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }
    }
}
