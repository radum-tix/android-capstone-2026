package ro.upb.summer.capstone.ui.generate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.ai
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ro.upb.summer.capstone.data.ai.AiRepository
import ro.upb.summer.capstone.data.decks.DeckRepository
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val aiRepository: AiRepository,
    private val deckRepository: DeckRepository
) : ViewModel() {
    private val _state = MutableStateFlow<GenerationState>(GenerationState.Idle)
    val state: StateFlow<GenerationState> = _state.asStateFlow()

    private val _saving = MutableStateFlow(false)
    val saving: StateFlow<Boolean> = _saving.asStateFlow()

    private var notes: String = ""

    fun generate(notes: String, imageUri: Uri?) {
        if (notes.isBlank() && imageUri == null) return
        this.notes = notes
        viewModelScope.launch {
            val bitmap = imageUri?.let { loadBitmap(it) }
            retry {
                aiRepository.generate(notes, bitmap).collect {
                    _state.value = it
                    if (it is GenerationState.Error) {
                        error("Failed")
                    }
                }
            }
        }
    }

    private suspend fun loadBitmap(uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
        runCatching {
            context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
        }.getOrNull()
    }

    fun save(title: String, onSaved: (String) -> Unit) {
        val cards = (state.value as? GenerationState.Complete)?.cards ?: return
        if (title.isBlank() || _saving.value) return
        viewModelScope.launch {
            _saving.value = true
            val deckId = deckRepository.saveDeck(title, notes, cards)
            onSaved(deckId)
            _saving.value = false
        }
    }

    private suspend fun <T> retry(
        maxRetry: Int = 3,
        initialDelay: Long = 5000L,
        block: suspend () -> T): T {

        var attempts = 0
        var delay = initialDelay

        while (attempts < maxRetry) {
            try {
                println("Starting request")
                return block()
                println("Request successful")
            } catch (exception: Exception) {
                println("Request failed: $attempts")
                // if needs retry
                attempts++
                delay(delay)
                delay *= 2
                println("Retrying request")
            }
        }
        error("Did not finish")
    }
}