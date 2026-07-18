package ro.upb.summer.capstone.ui.decks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ro.upb.summer.capstone.data.decks.DeckRepository
import ro.upb.summer.capstone.domain.Deck
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DeckListViewModel @Inject constructor(
    private val deckRepository: DeckRepository
) : ViewModel() {
    private val _state = MutableStateFlow<DeckListUiState>(DeckListUiState.Loading)
    val state: StateFlow<DeckListUiState>
        get() = _state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                deckRepository.decks.collect { decks ->
                    if (decks.isEmpty()) {
                        _state.value = DeckListUiState.Empty
                    } else {
                        _state.value = DeckListUiState.Success(decks)
                    }
                }
            } catch (exception: Exception) {
                _state.value = DeckListUiState.Error(exception.message ?: "I have no idea what's wrong")
            }
        }
    }

    fun createNewDeck() {
        viewModelScope.launch {
            deckRepository.createDeck(
                Deck(
                    id = "",
                    title = "No title available",
                    createdAt = Date(),
                    updatedAt = Date(),
                    noCards = 0
                )
            )
        }
    }
}