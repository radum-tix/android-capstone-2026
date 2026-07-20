package ro.upb.summer.capstone.ui.generate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
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
            //TODO: generate cards
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
            //TODO: save new deck
            _saving.value = false
        }
    }
}