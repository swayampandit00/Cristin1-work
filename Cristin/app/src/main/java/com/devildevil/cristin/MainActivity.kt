package com.devildevil.cristin

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.devildevil.cristin.service.WakeWordService
import com.devildevil.cristin.ui.CristinUI
import com.devildevil.cristin.utils.TTS
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private lateinit var tts: TTS

    private val permissionsToRequest: Array<String> by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.READ_PHONE_STATE
            )
        } else {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.values.all { it }
        if (allPermissionsGranted) {
            startWakeWordService()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TTS(this)
        greetUser()

        setContent {
            Surface(color = MaterialTheme.colorScheme.background) {
                CristinUI {
                    requestPermissionsAndStartService()
                }
            }
        }
    }

    private fun greetUser() {
        val timeOfDay = SimpleDateFormat("a", Locale.getDefault()).format(Date())
        val greeting = when (timeOfDay.lowercase()) {
            "am" -> "Good morning"
            "pm" -> "Good afternoon" // This can be improved for evening
            else -> "Hello"
        }

        val date = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date())

        tts.speak("$greeting! Today is $date, and the time is $time.")
    }

    private fun requestPermissionsAndStartService() {
        requestPermissionLauncher.launch(permissionsToRequest)
    }

    private fun startWakeWordService() {
        val serviceIntent = Intent(this, WakeWordService::class.java)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
