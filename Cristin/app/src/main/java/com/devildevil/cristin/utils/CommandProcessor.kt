package com.devildevil.cristin.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.MediaStore

class CommandProcessor(private val ctx: Context) {

    private val tts = TTS(ctx)

    fun process(cmd: String) {
        val lowerCaseCmd = cmd.lowercase()

        when {
            "call" in lowerCaseCmd -> {
                val name = lowerCaseCmd.replace("call", "").trim()
                tts.speak("Calling $name")
                callPerson(name)
                tts.speak("Call task completed.") // This is a simple confirmation
            }

            "play music" in lowerCaseCmd -> {
                tts.speak("Opening the music player.")
                playMusic()
            }

            "next song" in lowerCaseCmd -> {
                tts.speak("Playing the next song.")
                val intent = Intent("com.android.music.musicservicecommand")
                intent.putExtra("command", "next")
                ctx.sendBroadcast(intent)
            }

            "pause music" in lowerCaseCmd -> {
                tts.speak("Pausing the music.")
                val intent = Intent("com.android.music.musicservicecommand")
                intent.putExtra("command", "pause")
                ctx.sendBroadcast(intent)
            }

            "open whatsapp" in lowerCaseCmd -> {
                tts.speak("Opening WhatsApp.")
                openApp("com.whatsapp")
            }

            "open camera" in lowerCaseCmd -> {
                tts.speak("Opening the camera.")
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                ctx.startActivity(intent)
            }

            "send a message" in lowerCaseCmd -> {
                val recipient = lowerCaseCmd.substringAfter("to ").substringBefore(" saying")
                val message = lowerCaseCmd.substringAfter("saying ")
                tts.speak("Preparing a message for $recipient.")
                sendMessage(recipient, message)
                tts.speak("Message is ready. Please check and send it.")
            }

            else -> {
                tts.speak("Sorry, I didn't understand that command.")
            }
        }
    }

    private fun callPerson(name: String) {
        try {
            val contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(name))
            val projection = arrayOf(ContactsContract.PhoneLookup.NUMBER)
            val cursor = ctx.contentResolver.query(contactUri, projection, null, null, null)

            var number: String? = null
            if (cursor != null && cursor.moveToFirst()) {
                val numberIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER)
                if (numberIndex != -1) {
                    number = cursor.getString(numberIndex)
                }
                cursor.close()
            }

            if (number != null) {
                val intent = Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:$number")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                ctx.startActivity(intent)
            } else {
                tts.speak("Sorry, I couldn't find anyone named $name in your contacts.")
            }
        } catch (e: SecurityException) {
            tts.speak("I need permission to make phone calls.")
        }
    }

    private fun playMusic() {
        val intent = Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }

    private fun openApp(packageName: String) {
        val intent = ctx.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        } else {
            tts.speak("Sorry, I couldn't find that app on your phone.")
        }
    }

    private fun sendMessage(recipient: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"))
        intent.putExtra("sms_body", message)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ctx.startActivity(intent)
    }
}
