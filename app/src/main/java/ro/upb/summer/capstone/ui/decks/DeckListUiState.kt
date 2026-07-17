package ro.upb.summer.capstone.ui.decks

import ro.upb.summer.capstone.domain.Deck

sealed interface DeckListUiState {
    data object Loading : DeckListUiState
    data class Success(val decks: List<Deck>): DeckListUiState
    data object Empty : DeckListUiState
    data class Error(val message: String): DeckListUiState
}