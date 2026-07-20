package ro.upb.summer.capstone.domain

import kotlinx.serialization.Serializable
import java.util.Date

data class Card(
    val question: String,
    val answer: String,
    val createdAt: Date,
    val lastStudied: Date? = null
)

/** A flashcard as produced by Gemini's structured JSON response. */
@Serializable
data class GeneratedCard(
    val question: String = "",
    val answer: String = "",
)