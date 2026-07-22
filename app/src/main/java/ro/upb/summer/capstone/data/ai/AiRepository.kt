package ro.upb.summer.capstone.data.ai

import android.graphics.Bitmap
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import ro.upb.summer.capstone.data.config.ConfigRepository
import ro.upb.summer.capstone.domain.GeneratedCard
import ro.upb.summer.capstone.ui.generate.GenerationState
import javax.inject.Inject

class AiRepository @Inject constructor(
    private val firebaseAi: FirebaseAI,
    private val configRepository: ConfigRepository
) {
    fun generate(notes: String, bitmap: Bitmap? = null): Flow<GenerationState> = flow {
        emit(GenerationState.Loading)

        val model = buildModel()
        val response = model.generateContent(
            content {
                if (bitmap != null) {
                    image(bitmap)
                }
                text(notes)
            }
        )

        val text = response.text ?: error("No content was returned")
        val cards = Json.decodeFromString<List<GeneratedCard>>(text)
        if (cards.isEmpty()) {
            error("No content was returned")
        }
        emit(GenerationState.Complete(cards))
     }.catch { cause ->
         emit(GenerationState.Error(message = cause.message ?: "Something weally, weally bad happened"))
    }

    private fun buildModel() = firebaseAi
        .generativeModel(
            modelName = configRepository.modelName,
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = Schema.array(
                    items = Schema.obj(
                        properties = mapOf(
                            "question" to Schema.string(),
                            "answer" to Schema.string()
                        )
                    )
                )
                temperature = configRepository.temperature
            },
            systemInstruction = content {
                text(configRepository.systemPrompt)
            }
        )
}