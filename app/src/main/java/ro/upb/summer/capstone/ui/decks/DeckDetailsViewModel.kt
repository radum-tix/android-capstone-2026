package ro.upb.summer.capstone.ui.decks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ro.upb.summer.capstone.data.decks.CardRepository
import ro.upb.summer.capstone.data.decks.DeckRepository
import ro.upb.summer.capstone.domain.Card
import ro.upb.summer.capstone.domain.Deck
import ro.upb.summer.capstone.ui.Route
import javax.inject.Inject

@HiltViewModel
class DeckDetailViewModel @Inject constructor(
    decksRepo: DeckRepository,
    cardsRepo: CardRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val deckId = savedStateHandle.toRoute<Route.DeckDetails>().id

    val deck: StateFlow<Deck?> = decksRepo.observeDeck(deckId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val cards: StateFlow<List<Card>> = cardsRepo.observeCards(deckId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}