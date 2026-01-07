# Cristin AI - Android Voice Assistant

This is an Android voice assistant application named Cristin AI. It runs in the background, continuously listening for a wake word to perform commands.

## Features

- **Continuous Wake Word Detection:** Always listening for the hotword.
- **Background Service:** Runs as a persistent foreground service.
- **Speech-to-Text (STT) & Text-to-Speech (TTS):** Converts voice to commands and speaks responses.
- **Voice Commands:**
    - Make phone calls ("Call <contact name>").
    - Control music ("Play music", "Next song").
    - Open applications ("Open WhatsApp").

## Configuration Guide

To get the assistant fully working with your custom wake word, follow these steps.

### Step 1: Get Your Picovoice Access Key

The wake word detection is powered by Picovoice Porcupine. It requires an `accessKey` to work.

1.  Go to the [Picovoice Console](https://console.picovoice.ai/).
2.  Sign up or log in.
3.  On the dashboard, you will see your `AccessKey`. Copy it.
4.  Open the following file in your project:
    `app/src/main/java/com/devildevil/cristin/service/WakeWordService.kt`
5.  Find the `accessKey` variable and paste your key there:

    ```kotlin
    // Before
    val accessKey = "Rfaga69ASJiIDsebsLJfORdquxM5z3GaUe+Ns+0khi/ZYjpIfoDR+w=="

    // After (replace with your key)
    val accessKey = "YOUR_ACCESS_KEY_HERE"
    ```

### Step 2: Create a Custom Wake Word Model

The project is currently configured to respond to the built-in wake word **"Porcupine"**. To make it respond to **"Hello Cristin"**, you need to train a custom model.

1.  Go to the [Picovoice Console](https://console.picovoice.ai/).
2.  Navigate to the **Porcupine** page.
3.  Create a new model by typing your desired wake word (e.g., "Hello Cristin").
4.  Train the model and download the resulting `.ppn` file.

### Step 3: Add the Model File to Your Project

1.  In Android Studio, right-click on the `app/src/main` directory.
2.  Select **New > Directory**.
3.  Name the directory `assets`.
4.  Drag and drop your downloaded `.ppn` file (e.g., `Hello-Cristin.ppn`) into this new `assets` directory.

### Step 4: Update the Code to Use Your Custom Model

Finally, you need to tell the `WakeWordService` to use your new custom model instead of the built-in one.

1.  Open `app/src/main/java/com/devildevil/cristin/service/WakeWordService.kt`.
2.  Locate the `initPorcupine` function.
3.  Change the `.setKeyword()` line to `.setKeywordPath()` and provide the name of your file from the `assets` folder.

    ```kotlin
    // --- In the initPorcupine() function ---

    // Before: Using the built-in wake word
    porcupineManager = PorcupineManager.Builder()
        .setAccessKey(accessKey)
        .setKeyword(Porcupine.BuiltInKeyword.PORCUPINE) // This line will be changed
        .build(applicationContext, object : PorcupineManagerCallback {
            // ...
        })


    // After: Using your custom .ppn file
    porcupineManager = PorcupineManager.Builder()
        .setAccessKey(accessKey)
        .setKeywordPath("Hello-Cristin.ppn") // Change this to your file's name
        .build(applicationContext, object : PorcupineManagerCallback {
            // ...
        })
    ```

After following these steps, rebuild the app. The assistant will now respond to your custom wake word "Hello Cristin."
