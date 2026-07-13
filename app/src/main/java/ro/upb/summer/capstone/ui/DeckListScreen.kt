package ro.upb.summer.capstone.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ro.upb.summer.capstone.ui.theme.CapstoneStarterTheme

/**
 * Hardcoded deck list. Session 1 refactors this into a ViewModel with UiState.
 * Session 3 replaces the hardcoded list with a Firestore-backed Flow.
 */
private data class MockDeck(val id: String, val title: String, val cardCount: Int)

private val mockDecks = listOf(
    MockDeck("1", "Kotlin basics", 12),
    MockDeck("2", "Compose fundamentals", 8),
    MockDeck("3", "Firebase quickstart", 5),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckListScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your decks") })
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp),
        ) {
            items(mockDecks, key = { it.id }) { deck ->
                DeckCard(deck = deck)
            }
        }
    }
}

@Composable
private fun DeckCard(deck: MockDeck) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = deck.title,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "${deck.cardCount} cards",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeckListScreenPreview() {
    CapstoneStarterTheme {
        DeckListScreen()
    }
}