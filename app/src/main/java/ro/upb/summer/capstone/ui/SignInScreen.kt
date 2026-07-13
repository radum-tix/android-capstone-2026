package ro.upb.summer.capstone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ro.upb.summer.capstone.ui.theme.CapstoneStarterTheme

/**
 * Mock sign-in screen. Session 2 replaces the button with real Firebase Auth.
 * For now, tapping Continue simply navigates to DeckList.
 */
@Composable
fun SignInScreen(onSignedIn: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Study Companion",
            style = MaterialTheme.typography.displayLarge,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Turn your notes into flashcards.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(48.dp))
        Button(onClick = onSignedIn) {
            Text("Continue")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SignInScreenPreview() {
    CapstoneStarterTheme {
        SignInScreen(onSignedIn = {})
    }
}