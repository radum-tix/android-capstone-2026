package ro.upb.summer.capstone.data.decks

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import ro.upb.summer.capstone.domain.Card
import ro.upb.summer.capstone.domain.Deck

/**
 * A flashcard stored under `users/{uid}/decks/{deckId}/cards/{cardId}`.
 * Defaults are required for Firestore's no-arg deserialization.
 */
data class FirestoreCard(
    @DocumentId val id: String = "",
    val question: String = "",
    val answer: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val lastStudied: Timestamp? = null,
)

fun Card.toFirebase() = FirestoreCard(
    question = this.question,
    answer = this.answer,
    createdAt = Timestamp(this.createdAt),
    lastStudied = this.lastStudied?.let { Timestamp(it) }
)

fun FirestoreCard.fromFirebase() = Card(
    question = this.question,
    answer = this.answer,
    createdAt = this.createdAt.toDate(),
    lastStudied = this.lastStudied?.toDate(),
)