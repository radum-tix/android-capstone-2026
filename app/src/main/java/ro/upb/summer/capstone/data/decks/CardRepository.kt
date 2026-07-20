package ro.upb.summer.capstone.data.decks

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ro.upb.summer.capstone.domain.Card
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Thin repository over a deck's cards subcollection. Follows the same shape as
 * DeckRepository.
 */
@Singleton
class CardRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    fun observeCards(deckId: String): Flow<List<Card>> = callbackFlow {
        val uid = auth.currentUser?.uid ?: run {
            trySend(emptyList())
            close()
            return@callbackFlow
        }
        val registration = cardsCollection(uid, deckId)
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(
                    snapshot
                        ?.toObjects(FirestoreCard::class.java)
                        ?.map(FirestoreCard::fromFirebase)
                        ?: emptyList()
                )
            }
        awaitClose { registration.remove() }
    }

    private fun cardsCollection(uid: String, deckId: String) =
        firestore.collection("users").document(uid)
            .collection("decks").document(deckId)
            .collection("cards")
}