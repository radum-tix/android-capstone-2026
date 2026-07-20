package ro.upb.summer.capstone.ui.generate

import ro.upb.summer.capstone.domain.GeneratedCard

/** State of a structured flashcard generation. */
sealed interface GenerationState {
    data object Idle : GenerationState
    data object Loading : GenerationState
    data class Complete(val cards: List<GeneratedCard>) : GenerationState
    data class Error(val message: String) : GenerationState
}