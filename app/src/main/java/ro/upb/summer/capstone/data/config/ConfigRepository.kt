package ro.upb.summer.capstone.data.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

class ConfigRepository @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) {
    val modelName: String
        get() = remoteConfig.getString(KEY_MODEL)

    val temperature: Float
        get() = remoteConfig.getDouble(KEY_TEMPERATURE).toFloat()

    val systemPrompt: String
        get() = remoteConfig.getString(KEY_SYSTEM_PROMPT)

    companion object {
        const val KEY_MODEL = "gemini_model"
        const val KEY_TEMPERATURE = "gemini_temperature"
        const val KEY_SYSTEM_PROMPT = "gemini_system_prompt"

        val DEFAULTS = mapOf(
            KEY_MODEL to "gemini-3.1-flash-lite",
            KEY_TEMPERATURE to 0.7f,
            KEY_SYSTEM_PROMPT to "You are a study assistant that generates concise, accurate flashcards from student notes. Each question should be answerable in one sentence. You always add a trick question which is funny but not necessarily true."
        )
    }
}