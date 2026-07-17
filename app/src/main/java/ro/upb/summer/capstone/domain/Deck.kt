package ro.upb.summer.capstone.domain

import java.util.Date

data class Deck(
    val id: String,
    val title: String,
    val createdAt: Date,
    val updatedAt: Date,
    val noCards: Int
)