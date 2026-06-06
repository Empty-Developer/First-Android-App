# Greean Status

A health and wellness Android app that helps you track daily water intake, calculate calorie needs, browse food nutrition, explore a live map with real-time weather, and manage a personal profile  all backed by Firebase.

---

## Features

### 💧 Water Tracker (Home)
Track how much water you drink throughout the day with a visual 8-cell tracker. Tap **+** to log a glass and **−** to remove one. The tracker fills up cell by cell so you can see your progress at a glance.

### 🗺️ Map & Weather (Map)
An interactive Yandex Maps view centered on your area. On launch, the app fetches live weather data from OpenWeatherMap (temperature, conditions) and displays it in a dialog so you always know what to expect outside.

### 🔥 Calorie Calculator (Calculator)
Enter your **weight**, **height**, **age**, **sex**, and **activity level** to get a personalized daily calorie target using the Mifflin-St Jeor formula (BMR × activity multiplier). Results are shown instantly in a dialog.

### 🥗 Food Reference (Food)
Browse a searchable list of common foods with calories and macros (protein, carbs, fat) per serving. Use the search bar to filter the list in real time.

### 👤 User Profile (User)
Set your display name, write personal notes, and upload a profile photo from your gallery. All data (name, notes, and photo URL) is saved to **Firebase Realtime Database** and **Firebase Storage** and automatically reloaded the next time you open the app.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Minimum SDK | Android 7.0 (API 24) |
| Target SDK | Android 15 (API 35) |
| UI | Material Design 3, ConstraintLayout, Navigation Component |
| Maps | Yandex MapKit SDK 4.5 |
| Weather | OpenWeatherMap REST API (Retrofit 2 + Gson) |
| Image loading | Glide 4.16 |
| Image picking | ImagePicker 2.1 |
| Backend | Firebase Realtime Database, Firebase Firestore, Firebase Storage, Firebase Auth, Firebase Analytics |
| Build | Gradle 8.8, AGP 8.8, Kotlin Gradle plugin 1.9 |
| Splash screen | AndroidX Core SplashScreen 1.0 |

---

## Project Structure

```
app/
└── src/main/
    ├── java/com/example/greeanstatus/
    │   ├── HomeScreen.java          # Water tracker + bottom nav host
    │   ├── MapScreen.java           # Yandex map + live weather dialog
    │   ├── CalculatorScreen.java    # BMR / calorie calculator
    │   ├── FoodScreen.java          # Searchable food nutrition list
    │   ├── UserWindowScreen.java    # Profile: name, notes, photo
    │   └── api/
    │       ├── models/
    │       │   └── WeatherResponse.java
    │       └── services/
    │           └── WeatherApiService.java  # Retrofit interface
    └── res/
        ├── layout/                  # XML layouts for each screen
        ├── drawable/                # Icons and shape drawables
        ├── font/                    # Unbounded font family
        └── values/                  # Colors, strings, themes
```

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 11
- A Firebase project with **Realtime Database**, **Firestore**, and **Storage** enabled
- A [Yandex MapKit API key](https://developer.tech.yandex.ru/)
- An [OpenWeatherMap API key](https://openweathermap.org/api)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Empty-Developer/First-Android-App.git
   cd First-Android-App
   ```

2. **Add your Firebase config**
   - Go to your Firebase project → Project Settings → Download `google-services.json`
   - Place it at `app/google-services.json` (replacing the placeholder file)

3. **Set your API keys**

   In `MapScreen.java`, replace the placeholder values:
   ```java
   private final String MAPKIT_API_KEY = "YOUR_YANDEX_MAPKIT_KEY";
   private static final String WEATHER_API_KEY = "YOUR_OPENWEATHERMAP_KEY";
   ```

4. **Open in Android Studio**
   - Open the project root in Android Studio
   - Let Gradle sync finish
   - Run on an emulator (API 24+) or a physical device

---

## Firebase Rules (Quick Start)

For local development, you can use permissive Realtime Database rules:

```json
{
  "rules": {
    ".read": true,
    ".write": true
  }
}
```

> **Before going to production**, lock down these rules so users can only read and write their own data.

---

## Known Limitations

- The weather city is currently hardcoded to **Krasnoyarsk** in `MapScreen.java`. Change the `CITY` constant to use your own location.
- User authentication uses the **device ID** (Android ID) as a unique key — there is no login screen. All data is public to anyone with the same Firebase project.
- Food items in `FoodScreen` are hardcoded in Java. A future version could load them from Firestore.
