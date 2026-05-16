# Grama-Suvidha

Grama-Suvidha is an Android application built using Kotlin and Jetpack Compose. It aims to provide a platform for rural communities, featuring project displays, a feedback module, and role-based access (admin vs. user).

## Features

- **Modern UI**: Built entirely with Jetpack Compose.
- **Local Storage**: Uses Room Database for persistent data storage.
- **Navigation**: Utilizes Jetpack Navigation Compose for seamless in-app transitions.
- **Image Loading**: Integrates Coil for efficient image loading.
- **Role-Based Access**: Separates UI and functionalities for admins and regular users.

## Prerequisites

To open and run this project, you will need:
- [Android Studio](https://developer.android.com/studio) (latest version recommended)
- JDK 17
- Minimum Android SDK: 24 (Android 7.0)
- Target Android SDK: 34 (Android 14)

## Getting Started

1. **Clone or Download the Repository:**
   Open a terminal and run:
   ```bash
   git clone <repository-url>
   ```
   Or download the source code and extract it.

2. **Open in Android Studio:**
   - Launch Android Studio.
   - Click on **Open** (or **Open an Existing Project**).
   - Navigate to the directory where you cloned/extracted the project (`Grama-Suvidha`).
   - Select the folder and click **OK**.

3. **Sync Gradle:**
   - Android Studio will automatically start syncing the project dependencies via Gradle.
   - Wait for the sync to complete. If prompted, allow Android Studio to download any missing SDKs or tools.

4. **Run the Application:**
   - Connect a physical Android device via USB (with USB Debugging enabled) or start an Android Virtual Device (AVD).
   - Select your target device in the run configurations dropdown.
   - Click the **Run** button (green play icon) in the toolbar, or press `Shift + F10`.
   - The app will compile, install, and launch on your device/emulator.

## Tech Stack

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose
- **Database**: Room
- **Image Loading**: Coil
- **JSON Parsing**: Gson
- **Architecture**: MVVM (Model-View-ViewModel)

## Building from Command Line

If you prefer building without the IDE, you can use the Gradle wrapper:

**Windows:**
```cmd
gradlew.bat assembleDebug
```

**macOS/Linux:**
```bash
./gradlew assembleDebug
```

The generated APK will be located in `app/build/outputs/apk/debug/`.
