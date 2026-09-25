# TestWeather

Android app that shows a Google Map centered on the user's location and the current
weather for that location from [OpenWeatherMap](https://openweathermap.org/current).

## Requirements

- JDK 17+
- Android SDK with platform 35 (Android Studio Ladybug or newer)

## API keys

Keys are not stored in the repository. Add them to `local.properties` in the project root
(the file is ignored by git):

```properties
MAPS_API_KEY=your_google_maps_android_key
OWM_API_KEY=your_openweathermap_key
```

- Google Maps key: <https://developers.google.com/maps/documentation/android-sdk/get-api-key>
- OpenWeatherMap key: <https://home.openweathermap.org/api_keys>

## Build and test

```sh
./gradlew assembleDebug          # build the debug APK
./gradlew testDebugUnitTest      # JVM unit tests (model, network client, formatter)
./gradlew connectedAndroidTest   # instrumented tests, needs a device or emulator
```
