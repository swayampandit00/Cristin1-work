package com.devildevil.cristin.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TTS(ctx: Context) : TextToSpeech.OnInitListener {

    private val tts: TextToSpeech = TextToSpeech(ctx, this)
    private var isInitialized = false
    private val messageQueue = mutableListOf<String>()

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
            isInitialized = true
            // Speak any queued messages
            messageQueue.forEach { speak(it, TextToSpeech.QUEUE_ADD) }
            messageQueue.clear()
        }
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (isInitialized) {
            tts.speak(text, queueMode, null, null)
        } else {
            // Queue the message if not initialized
            messageQueue.add(text)
        }
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }
}
