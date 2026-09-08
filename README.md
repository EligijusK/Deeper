# Deeper Technical Task

A Kotlin Multiplatform application developed as a technical assignment for Deeper.

The application authenticates against the Deeper staging API, displays the user's scans, loads bathymetry data for a selected scan, and visualizes the result on Google Maps on both Android and iOS.

## Features

- User authentication with the Deeper staging API
- Display of available scans
- Bathymetry data loading for selected scans
- Bathymetry visualization using depth-colored polygons
- Scan location marker
- Location-only display when bathymetry geometry is unavailable
- Depth legend matching rendered polygon colors
- Loading, error and retry states
- In-memory and persistent Room bathymetry caching
- Persisted bathymetry availability in the scan list
- 24-hour cache freshness policy
- Back navigation between scan details and the scan list
- Google Maps support on Android and iOS
- Android API 26+
- GitHub Actions CI for Android and iOS

## Architecture

The project uses a layered architecture with separation between data, domain and presentation logic.

```text
Presentation
    ↓
Use Cases
    ↓
Repository Interfaces
    ↓
Repository Implementations
    ↓
Remote API / Room Cache
```

### Data layer

Responsible for remote API communication, local persistence and conversion of external data into application models.

Includes:

- Ktor HTTP client
- API implementation and DTOs
- DTO-to-domain mappers
- Room database, DAO and cache entity
- JSON cache encoding/decoding

### Domain layer

Contains application models and business-facing abstractions used by the presentation layer.

Includes:

- Domain models
- Repository interfaces
- Use cases
- Request outcomes and error models

### Presentation layer

Implemented using Compose Multiplatform.

Includes:

- ViewModels
- StateFlow-based UI state
- Route composables
- Stateless screen composables
- Shared bathymetry presentation logic
- Platform-specific Google Maps implementations

Dependency injection is handled using Koin.

## Kotlin Multiplatform

The project uses Kotlin Multiplatform to share the majority of the application across Android and iOS.

Most application code is located in `commonMain`, including:

- domain models
- repository contracts and shared repository logic
- use cases
- ViewModels
- Compose UI
- networking
- serialization
- Room database declarations
- dependency injection configuration

Platform source sets are used where platform APIs are required.

### Android-specific code

Android provides:

- Google Maps Compose rendering
- Room database creation using Android application context
- Android-specific Ktor engine configuration

### iOS-specific code

iOS provides:

- Google Maps SDK for iOS integration
- a Swift implementation of the shared map controller interface
- `UIKitView` interop for displaying the native `GMSMapView` inside Compose
- Room database creation in the application sandbox
- Darwin Ktor engine

The shared Compose code supplies polygon coordinates, colors, markers and camera instructions through an `IosMapFactory` / `IosMapController` bridge. Swift owns the native Google Maps objects and performs the actual map rendering.

## Technologies

- Kotlin
- Kotlin Multiplatform
- Compose Multiplatform
- Material 3
- Ktor
- Kotlin Coroutines
- StateFlow
- Koin
- Kotlin Serialization
- AndroidX Room 3
- Bundled SQLite driver
- Google Maps Compose
- Google Maps SDK for iOS
- Swift / UIKit interop
- Kotlin Test
- Ktor MockEngine
- GitHub Actions

## Authentication

The application authenticates using the Deeper staging API.

After a successful login, the returned authentication token is used when requesting bathymetry data for individual scans.

Authentication and request failures are converted into application-level error states instead of exposing HTTP implementation details directly to the UI.

Handled states include:

- incorrect credentials
- access denied
- network failure
- server failure
- unknown error

Coroutine cancellation is propagated instead of being converted into a request error.

## Bathymetry

Bathymetry data is requested when the user opens a scan unless a fresh cached result already exists.

The returned geometry is converted from the API representation into domain models before reaching the presentation layer.

GeoJSON-style coordinates returned by the API are interpreted as:

```text
[longitude, latitude, depth]
```

and mapped internally to latitude/longitude domain points.

Depth values are grouped into fixed ranges and represented using progressively darker colors:

```text
0–1 m
1–2 m
2–3 m
3–4 m
4–5 m
5–6 m
6–8 m
8–10 m
10–12 m
12+ m
```

The same depth configuration is reused by map rendering and the depth legend.

## Scans Without Bathymetry Geometry

A scan may contain geographic information without generated bathymetry polygons.

These scans are not treated as invalid.

If no renderable bathymetry geometry is available, the application displays the available scan location on the map instead.

```text
Bathymetry geometry available
→ polygons + scan marker + depth legend

No bathymetry geometry
→ scan marker only
```

## Bathymetry Availability

The login response does not provide reliable information indicating whether a scan has generated bathymetry, so the application does not pre-fetch the full geometry for every scan.

Availability begins as:

```text
UNKNOWN
```

After a bathymetry response is loaded:

```text
geometry available → AVAILABLE
no geometry        → NOT_AVAILABLE
```

The cache stores `hasBathymetry` separately from the full JSON response. This allows the scan list to load persisted availability using a lightweight Room query without decoding all cached polygon geometry.

As a result, cached availability can be shown again after the application restarts.

## Caching

Successfully loaded bathymetry responses are cached in two layers:

```text
Memory
   ↓ miss / expired
Room
   ↓ miss / expired
API
```

### Memory cache

The in-memory cache avoids repeated Room reads and JSON decoding during the same application process.

### Room cache

Room persists the serialized bathymetry response together with:

- `scanId`
- cache timestamp
- `hasBathymetry`

This allows bathymetry data to survive application restarts.

A cached item is considered fresh for 24 hours. Fresh memory or Room data is returned without another API request. If no fresh cache is available, the application requests the data from the API and updates both cache layers.

Failed API requests are not stored as successful cache entries, allowing later retries.

Potentially large JSON serialization, deserialization and domain mapping work is moved to `Dispatchers.Default`.

## Error Handling

Remote results are converted into explicit application outcomes.

The application handles:

- unauthorized requests
- forbidden requests
- network errors
- server errors
- unknown errors

The bathymetry screen exposes a retry action when loading fails.

## Dependency Injection

Koin is used for dependency injection.

The main dependency chain is:

```text
HttpClient
    ↓
DeeperApiInterface
    ↓
Repositories
    ↓
Use Cases
    ↓
ViewModels
    ↓
Compose UI
```

Stateful repositories and the Room database are registered as singleton instances, while use cases and ViewModels are resolved through Koin.

The database module uses an `expect` / `actual` platform implementation so Android and iOS can create Room with the correct writable database location without passing platform context through the shared UI.

## Google Maps

The shared UI exposes a common `BathymetryMap` contract, while each platform performs rendering with its native Google Maps integration.

### Android

Android uses Google Maps Compose directly from `androidMain`.

It renders:

- depth-colored polygons
- polygon stroke colors
- scan location marker
- camera bounds around bathymetry geometry
- location-focused camera fallback when geometry is unavailable

### iOS

iOS uses the Google Maps SDK for iOS from Swift Package Manager.

The Xcode project references:

```text
https://github.com/googlemaps/ios-maps-sdk
```

with the `GoogleMaps` product.

The map flow is:

```text
Compose shared code
    ↓
IosMapController interface
    ↓
IOSMapFactory.swift
    ↓
GMSMapView / GMSPolygon / GMSMarker
```

The native map view is returned to Compose and hosted through `UIKitView`.

## Running the Application

### Android requirements

- Android Studio
- Android device or emulator
- Android API 26 or newer
- Internet connection
- Google Maps API key with Maps SDK for Android enabled

### Android Google Maps API key

Create or update `local.properties` in the project root:

```properties
MAPS_API_KEY=YOUR_ANDROID_GOOGLE_MAPS_API_KEY
```

`local.properties` is ignored by Git and must not be committed.

For a real project, use a dedicated Android key and restrict it to the Android application package and signing certificate.

### iOS requirements

- macOS
- Xcode
- iOS simulator or device
- Internet connection
- Google Maps API key with Maps SDK for iOS enabled

The Google Maps Swift package is already referenced by the Xcode project. Xcode should resolve it automatically when the project is opened. If it needs to be added manually, use:

```text
https://github.com/googlemaps/ios-maps-sdk
```

and add the `GoogleMaps` product to the iOS application target.

### iOS Google Maps API key

Create this local file:

```text
iosApp/Configuration/Secrets.xcconfig
```

with:

```properties
MAPS_API_KEY=YOUR_IOS_GOOGLE_MAPS_API_KEY
```

Do not wrap the value in quotes.

`Debug.xcconfig` and `Release.xcconfig` optionally include `Secrets.xcconfig`. `Info.plist` reads the build setting through:

```text
$(MAPS_API_KEY)
```

and `AppDelegate` passes the resolved value to `GMSServices.provideAPIKey(...)` during application startup.

`Secrets.xcconfig` is ignored by Git and must not be committed.

For a real project, use a separate iOS key and restrict it to Maps SDK for iOS and the application bundle identifier.

## GitHub Actions

The repository contains CI for both `main` and `Dev`.

### Android CI

The Android job:

1. creates `local.properties` from the `MAPS_API_KEY` repository secret
2. runs Gradle tests
3. builds the Android debug APK
4. uploads the APK as a workflow artifact

Required repository secret:

```text
MAPS_API_KEY
```

### iOS CI

The iOS job creates the ignored `Secrets.xcconfig` file at build time from a GitHub Actions secret:

```text
IOS_MAPS_API_KEY
```

Conceptually:

```text
GitHub Actions secret
    ↓
Secrets.xcconfig
    ↓
Debug/Release.xcconfig
    ↓
Info.plist
    ↓
GMSServices.provideAPIKey(...)
```

The actual API key therefore does not need to be stored in the repository.

For signed non-PR builds, the workflow also installs the configured Apple certificate and provisioning profile, creates an archive, exports an IPA and uploads it as a workflow artifact.

## Testing

Tests are located in the shared test source set and use Kotlin Test together with test utilities such as Ktor MockEngine and coroutine testing support.

Relevant test areas include API handling, mapping, repository behavior, caching, availability and ViewModel state.

Run tests with:

```bash
./gradlew test
```

Build the Android debug APK with:

```bash
./gradlew :androidApp:assembleDebug
```

A full Gradle build can be run with:

```bash
./gradlew build
```

## Project Structure

```text
Deeper/
├── androidApp/
│   └── Android application entry point
│
├── iosApp/
│   ├── Configuration/
│   │   ├── Config.xcconfig
│   │   ├── Debug.xcconfig
│   │   └── Release.xcconfig
│   └── iosApp/
│       ├── AppDelegate.swift
│       ├── ContentView.swift
│       ├── IOSMapFactory.swift
│       └── Info.plist
│
└── shared/
    └── src/
        ├── commonMain/
        │   └── kotlin/com/eligijus/deeper/
        │       ├── data/
        │       │   ├── local/
        │       │   ├── mapper/
        │       │   └── remote/
        │       ├── domain/
        │       ├── presentation/
        │       └── di/
        ├── androidMain/
        │   ├── Android Room setup
        │   └── Google Maps Compose implementation
        ├── iosMain/
        │   ├── iOS Room setup
        │   ├── Darwin networking
        │   └── Compose/UIKit Google Maps bridge
        └── commonTest/
            └── Shared tests
```

## Design Decisions

### Lazy bathymetry loading

Bathymetry responses can contain a large amount of polygon geometry. Loading every scan immediately after login would create unnecessary network traffic, CPU work and memory usage.

The application therefore loads the full bathymetry response only when a scan is selected.

### Persistent cache with lightweight status metadata

The full response is stored as serialized JSON because the application retrieves it as one object by scan ID rather than performing relational queries over individual polygon points.

`hasBathymetry` is stored as a separate Room column so the scan list can restore availability cheaply without parsing the geometry JSON.

### Domain models separate from DTOs

Network DTOs are converted into domain models before reaching the presentation layer.

This keeps the UI independent from the remote API representation and centralizes details such as GeoJSON coordinate ordering and invalid geometry handling.

### Platform-specific map rendering

Bathymetry data and visual rules are shared, while the actual map SDK integration stays platform-specific.

Android uses Google Maps Compose. iOS uses a small Kotlin/Swift bridge so Swift can work directly with the native Google Maps SDK while Compose continues to own the shared screen and state.

### Stateless screen composables

Where practical, screen rendering is separated from ViewModel retrieval and dependency injection.

```text
ScanListRoute
    ↓
ScanListScreen

BathymetryRoute
    ↓
BathymetryScreen
```

This keeps screens focused on rendering state and makes them easier to preview and test.

## Known Limitations

This project was built as a technical assignment rather than a production application.

Current simplifications include:

- a fixed 24-hour cache freshness policy
- lightweight custom navigation instead of a navigation framework
- bathymetry cache entries keyed by scan ID
- staging API URLs configured directly in the API implementation
- production-level observability and database migration strategy are outside the scope of the task

## Author

Eligijus Kiudys
