package ro.upb.summer.capstone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ro.upb.summer.capstone.ui.auth.SignInScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.AuthRoute
    ) {
        composable<Route.AuthRoute> {
            SignInScreen(onSignedIn = {
                // i need to navigate to the deckList path
                navController.navigate(Route.DeckList) {
                    popUpTo(Route.AuthRoute) {
                        inclusive = true
                    }
                }
            })
        }
        composable<Route.DeckList> {
            DeckListScreen {
                navController.navigate(Route.DeckDetails(it.id))
            }
        }
        composable<Route.DeckDetails> { entry ->
            val route: Route.DeckDetails = entry.toRoute()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("you're on the deck details ${route.id} yay!")
            }
        }
    }
}

sealed interface Route {

    @Serializable
    data object AuthRoute : Route

    @Serializable
    data object DeckList : Route

    @Serializable
    data class DeckDetails(val id: String) : Route
}