package ro.upb.summer.capstone.data.decks

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import ro.upb.summer.capstone.domain.Deck
import javax.inject.Inject

class DeckRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    val decks: Flow<List<Deck>> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = firestore.collection("users")
            .document(uid)
            .collection("decks")
            .orderBy("updatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val results = snapshots
                    ?.toObjects(FirestoreDeck::class.java)
                    ?.map { it.fromFirebase() } ?: emptyList()

                trySend(results)
            }

        awaitClose { listener.remove() }
    }

    fun observeDeck(deckId: String): Flow<Deck?> = callbackFlow {
        val uid = firebaseAuth.currentUser?.uid ?: run {
            trySend(null)
            close()
            return@callbackFlow
        }
        val registration = firestore.collection("users")
            .document(uid)
            .collection("decks")
            .document(deckId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                trySend(snapshot
                    ?.toObject(FirestoreDeck::class.java)
                    ?.fromFirebase())
            }
        awaitClose { registration.remove() }
    }

    suspend fun createDeck(deck: Deck) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        val firestoreDeck = deck.toFirebase()

        val reference = firestore.collection("users")
            .document(uid)
            .collection("decks")
            .document()

        reference.set(firestoreDeck).await()
    }
}