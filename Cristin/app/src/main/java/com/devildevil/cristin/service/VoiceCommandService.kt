package com.devildevil.cristin.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.devildevil.cristin.utils.CommandProcessor
import com.devildevil.cristin.utils.TTS

class VoiceCommandService : Service() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var tts: TTS

    override fun onCreate() {
        super.onCreate()
        tts = TTS(this)
        initSpeechRecognizer()
    }

    private fun initSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Log.d("VoiceCommandService", "Listening for command...")
                tts.speak("Listening...")
            }

            override fun onResults(results: Bundle?) {
                val commands = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!commands.isNullOrEmpty()) {
                    val command = commands[0]
                    Log.d("VoiceCommandService", "Command received: $command")
                    CommandProcessor(applicationContext).process(command)
                }
                stopSelf() // Stop the service after processing the command
            }

            override fun onError(error: Int) {
                Log.e("VoiceCommandService", "Error listening for command: $error")
                tts.speak("Sorry, I didn't get that.")
                stopSelf()
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListeningForCommand()
        return START_NOT_STICKY
    }

    private fun startListeningForCommand() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
        }
        speechRecognizer.startListening(intent)
    }

    override fun onDestroy() {
        speechRecognizer.destroy()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
