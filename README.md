# Proverbs Widget

An Android home screen widget that shows a random verse from the Book of Proverbs (KJV) in a glassmorphism-styled card.

- Auto-refreshes to a new random verse once a day
- Tap the widget anytime to shuffle a new verse
- Resizable — small and large sizes both adapt (title/refresh icon hide when compact, text scales up when large)
- All 915 verses are bundled locally as JSON — no internet needed at runtime

## Install on your phone

### Option A: Build and install with a computer (recommended)

Requires: [Android Studio](https://developer.android.com/studio) (or just the Android SDK + JDK 17) and a phone with **USB debugging** enabled (Settings → About phone → tap "Build number" 7 times → Developer options → USB debugging).

1. Clone the repo and open the `android/` folder in Android Studio, **or** build from the command line:
   ```
   cd android
   ./gradlew assembleDebug
   ```
2. Connect your phone via USB and confirm the debugging prompt on the device.
3. Install the built APK:
   ```
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
   (Or in Android Studio, just click **Run**.)

### Option B: Sideload a prebuilt APK

1. Build the APK as above (or get `app-debug.apk` from someone who has), and transfer it to your phone (email, cloud drive, USB transfer).
2. Tap the APK file on your phone and allow "install from unknown sources" when prompted.
3. Install it.

### Add the widget to your home screen

1. Long-press an empty area of your home screen.
2. Tap **Widgets**.
3. Find **Proverbs** in the list.
4. Drag it onto your home screen, then long-press it to resize as you like.

## Project structure

```
android/                     Android Studio project (Kotlin)
  app/src/main/
    java/com/proverbs/widget/
      ProverbsWidgetProvider.kt  Widget logic: verse selection, resizing, tap-to-refresh
      VerseRepository.kt         Loads/parses the bundled verse data
      MainActivity.kt            Minimal launcher screen with install instructions
    res/                         Layout, glass-card drawable, strings/colors
    assets/proverbs_data.json    All 915 Proverbs verses (KJV)
proverbs_data.json           Source copy of the verse data
```

## Data source

Verse text was fetched once from [bible-api.com](https://bible-api.com) (KJV translation) and is bundled locally — the widget itself makes no network calls.
