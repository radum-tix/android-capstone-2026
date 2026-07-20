package ro.upb.summer.capstone.ui.decks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ro.upb.summer.capstone.ui.common.EmptyState
import ro.upb.summer.capstone.ui.common.FlipCard

/**
 * Deck detail: the deck's cards as tap-to-flip cards, plus rename/delete.
 * The Generate FAB is a placeholder until the AI flow lands in Phase 12.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckDetailScreen(
    onBack: () -> Unit,
    onGenerate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DeckDetailViewModel = hiltViewModel(),
) {
    val deck by viewModel.deck.collectAsStateWithLifecycle()
    val cards by viewModel.cards.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = deck?.title ?: "Deck",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onGenerate,
                icon = { Icon(Icons.Outlined.AutoAwesome, contentDescription = null) },
                text = { Text("Generate") },
            )
        },
    ) { innerPadding ->
        if (cards.isEmpty()) {
            EmptyState(
                icon = Icons.Outlined.Style,
                title = "No cards yet",
                subtitle = "Generate flashcards from your notes to fill this deck.",
                action = {
                    Button(
                        onClick = onGenerate,
                        modifier = Modifier.padding(top = 24.dp),
                    ) {
                        Text("Generate cards")
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(cards, key = { it.question }) { card ->
                    FlipCard(card = card)
                }
            }
        }
    }
}