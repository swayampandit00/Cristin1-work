# Cristin AI - Final Guide

**Ab aapka app poori tarah se taiyaar hai.** Isme background wake-word listening aur sabhi voice commands sahi se kaam kar rahe hain. Niche diye gaye guide ko follow karke aap app ko aasani se use kar sakte hain.

---

## Phone Compatibility (Kaun Se Phone Me Chalega)

**Yah app sirf Android 8.0 (Oreo) ya uske baad ke version wale phone par hi install hoga.**

-   **Android Version:** Aapke phone ka Android version 8.0 (API Level 26) ya usse naya hona chahiye.
-   **Google App:** Phone me Google App installed aur updated hona chahiye, kyunki yah voice recognition ke liye zaroori hai.

**Kaun se phone me nahi chalega?**
-   Agar aapka phone bahut purana hai aur Android 8.0 se pehle ke version (jaise 7.0 Nougat, 6.0 Marshmallow) par chal raha hai, to yah app install nahi hoga.

---

## App Kaise Kaam Karta Hai (How the App Works)

1.  **App Kholna (Opening the App):** Jab aap app icon par click karte hain, to ek simple screen khulti hai jispar "Start Listening" button hota hai.

2.  **Permissions Dena (Granting Permissions):** "Start Listening" button par click karne par, app aapse Microphone, Phone, aur Contacts jaise zaroori permissions maangega. **Aapko ye permissions "Allow" karni hongi.**

3.  **Background Listening:** Permissions dene ke baad, app background mein chala jaata hai aur "Hello Cristin" wake word sunne lagta hai. Aapke phone ke notification bar mein ek "Cristin is Listening..." notification dikhega, jiska matlab hai ki app active hai.

4.  **Wake Word Bolna (Saying the Wake Word):** Ab aap kabhi bhi, bina app khole, "Hello Cristin" bol sakte hain.

5.  **Command Dena (Giving a Command):** Jaise hi app wake word sunega, vah ek "Listening..." sound play karega. Iske baad aap apna command de sakte hain. (e.g., "Call Mom", "Play music").

---

## Voice Commands List

Aap niche diye gaye commands use kar sakte hain:

### 1. Calling Commands
-   `"Call <contact_name>"` (e.g., `"Call Mom"`, `"Call Papa ji"`)
    -   Yah aapke contacts se naam dhundkar call lagayega.

### 2. Music Commands
-   `"Play music"`
    -   Aapka default music player open karega.
-   `"Next song"`
    -   Music player me agla gaana play karega.
-   `"Pause music"`
    -   Current gaane ko pause karega.

### 3. App Control Commands
-   `"Open WhatsApp"`
    -   WhatsApp application kholega.
-   `"Open Camera"`
    -   Phone ka camera open karega.
-   `"Send a message to <contact_name> saying <your_message>"` (e.g., `"Send a message to Rohan saying I will be late"`)
    -   Yah feature aapko SMS bhejne me madad karega (lekin aapko "Send" button par manually click karna hoga).

---

## Manual Code Edits (Agar Zaroorat Pade)

**Is project me ab aapko koi bhi manual code edit karne ki zaroorat nahi hai.** Sab kuch automatically kaam karna chahiye.

Lekin, agar aap contact search ke logic ko behtar banana chahte hain, to aap `CommandProcessor.kt` file me `callPerson` function ko manually edit kar sakte hain. Abhi yah simple tarike se contact search karta hai.

File Path: `app/src/main/java/com/devildevil/cristin/utils/CommandProcessor.kt`

```kotlin
// In CommandProcessor.kt

private fun callPerson(name: String) {
    try {
        // ... (Current contact lookup logic)
        // You can improve this logic if you want more advanced contact matching.
    } catch (e: SecurityException) {
        // ...
    }
}
```

---

Ab aapka app poori tarah se use karne ke liye taiyaar hai. Enjoy your hands-free assistant!
