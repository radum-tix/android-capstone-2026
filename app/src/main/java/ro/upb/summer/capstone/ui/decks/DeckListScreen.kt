package ro.upb.summer.capstone.ui.decks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ro.upb.summer.capstone.domain.Deck
import ro.upb.summer.capstone.ui.common.EmptyState
import ro.upb.summer.capstone.ui.common.ErrorState
import ro.upb.summer.capstone.ui.common.LoadingState
import ro.upb.summer.capstone.ui.common.toRelativeString


// ============================================================================
// DECK LIST
// ============================================================================

/**
 * A single row in the deck list.
 */
@Composable
fun DeckCard(
    deck: Deck,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = deck.title.ifBlank { "Untitled deck" },
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${deck.noCards} " + if (deck.noCards == 1) "card" else "cards",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(6.dp))
                Text("\u00B7", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(6.dp))
                Text(
                    text = deck.updatedAt.toRelativeString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/**
 * The deck list screen. Consumes DeckListUiState from your ViewModel.
 * Wire viewModel.state to this composable in Block B.
 *
 * NOTE for Block D: the AnimatedContent + contentKey pattern goes here later.
 * For now this uses a plain when. Session 3 Block D teaches the refactor.
 */
@Composable
fun DeckListScreen(
    onDeckClick: (String) -> Unit,
    viewModel: DeckListViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    InnerDeckListScreen(
        onDeckClick,
        onNewDeck = { viewModel.createNewDeck() },
        state,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InnerDeckListScreen(
    onDeckClick: (String) -> Unit,
    onNewDeck: () -> Unit,
    state: DeckListUiState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Your decks") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { onNewDeck() },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text("New deck") },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (state) {
                DeckListUiState.Loading -> LoadingState(label = "Loading your decks")
                DeckListUiState.Empty -> EmptyState(
                    title = "No decks yet",
                    subtitle = "Tap New deck to get started.",
                    icon = Icons.Outlined.Style,
                )
                is DeckListUiState.Success -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.decks, key = { it.id }) { deck ->
                        DeckCard(deck = deck, onClick = { onDeckClick(deck.id) })
                    }
                }
                is DeckListUiState.Error -> ErrorState(
                    message = state.message,
                    onRetry = { },
                )
            }
        }
    }
}