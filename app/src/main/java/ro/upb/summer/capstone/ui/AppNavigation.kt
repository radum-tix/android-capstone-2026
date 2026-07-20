package ro.upb.summer.capstone.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import ro.upb.summer.capstone.ui.auth.SignInScreen
import ro.upb.summer.capstone.ui.decks.DeckDetailScreen
import ro.upb.summer.capstone.ui.decks.DeckListScreen
import ro.upb.summer.capstone.ui.generate.GenerateScreen

@Composable
fun AppNavigation(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val isSignedIn by viewModel.isSignedIn.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (isSignedIn) Route.DeckList else Route.Auth
    ) {
        composable<Route.Auth> {
            SignInScreen(onSignedIn = {
                // i need to navigate to the deckList path
                navController.navigate(Route.DeckList) {
                    popUpTo(Route.Auth) {
                        inclusive = true
                    }
                }
            })
        }
        composable<Route.DeckList> {
            DeckListScreen(onDeckClick = {
                navController.navigate(Route.DeckDetails(it))
            })
        }
        composable<Route.DeckDetails> {
            // Now the deck id is read directly in the view model
            // val route: Route.DeckDetails = it.toRoute()
            DeckDetailScreen(
                onBack = navController::popBackStack,
                onGenerate = { navController.navigate(Route.Generate) }
            )
        }
        composable<Route.Generate> {
            GenerateScreen(
                onBack = navController::popBackStack,
                onSaved = { deckId ->
                    navController.navigate(Route.DeckDetails(deckId)) {
                        popUpTo<Route.Generate> { inclusive = true }
                    }
                },
            )
        }
    }
}

sealed interface Route {

    @Serializable
    data object Auth : Route

    @Serializable
    data object DeckList : Route

    @Serializable
    data class DeckDetails(val id: String) : Route

    @Serializable
    data object Generate : Route
}