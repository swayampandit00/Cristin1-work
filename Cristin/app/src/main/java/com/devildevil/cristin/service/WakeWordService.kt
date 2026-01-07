package com.devildevil.cristin.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class WakeWordService : Service() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        createNotif()
        initSpeechRecognizer()
    }

    private fun initSpeechRecognizer() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            Log.e("WakeWordService", "Speech recognition is not available on this device.")
            stopSelf()
            return
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        val listener = object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                restartListening()
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && !matches.isEmpty()) {
                    for (match in matches) {
                        if (match.lowercase().contains("hello cristin")) {
                            Log.d("WakeWordService", "Wake word detected!")

                            speechRecognizer.stopListening()

                            // FIX: Start the command service correctly using startService
                            val commandServiceIntent = Intent(applicationContext, VoiceCommandService::class.java)
                            startService(commandServiceIntent)

                            // Restart listening after a delay to allow the command to be processed
                            handler.postDelayed({ startListening() }, 5000)
                            return
                        }
                    }
                }
            }

            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                restartListening()
            }

            override fun onError(error: Int) {
                // Don't log common errors, just restart
                restartListening()
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
        speechRecognizer.setRecognitionListener(listener)
    }

    private fun restartListening() {
        // A short delay to prevent rapid-fire restarts
        handler.postDelayed({ startListening() }, 250)
    }

    private fun startListening() {
        try {
            speechRecognizer.startListening(recognizerIntent)
        } catch (e: Exception) {
            Log.e("WakeWordService", "Error starting to listen: ${e.message}")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startListening()
        return START_STICKY
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        speechRecognizer.destroy()
        super.onDestroy()
    }

    private fun createNotif() {
        val channelId = "cristin_ai_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Cristin AI",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }

        val notification: Notification =
            Notification.Builder(this, channelId)
                .setContentTitle("Cristin is Listening…")
                .setSmallIcon(android.R.drawable.ic_btn_speak_now)
                .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
