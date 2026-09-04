# Deeper Technical Task

A Kotlin Multiplatform application developed as a technical assignment for Deeper.

The application authenticates against the Deeper staging API, displays the user's scans, loads bathymetry data for a selected scan, and visualizes the result on Google Maps.

## Features

- User authentication with the Deeper staging API
- Display of available scans
- Bathymetry data loading for selected scans
- Bathymetry visualization using depth-colored polygons
- Scan location marker
- Location-only display when bathymetry geometry is unavailable
- Depth legend matching rendered polygon colors
- Loading, error and retry states
- In-memory bathymetry caching
- Bathymetry availability shown in the scan list after a scan has been loaded
- Back navigation between scan details and the scan list
- Support for Android API 26+

## Architecture

The project uses a layered architecture with separation between data, domain and presentation logic.

```text
Presentation
    ↓
Use Cases
    ↓
Repository Interfaces
    ↓
Repositories
    ↓
Remote API
```

### Data layer

Responsible for communication with the remote API and conversion of network responses.

Includes:

- Ktor HTTP client
- API implementation
- DTOs
- DTO-to-domain mappers
- Repository implementations

### Domain layer

Contains application logic independent of the UI and network implementation.

Includes:

- Domain models
- Repository interfaces
- Use cases
- Request outcomes and error models

### Presentation layer

Implemented using Compose Multiplatform.

Includes:

- ViewModels
- UI state classes
- Route composables
- Stateless screen composables
- Google Maps bathymetry visualization

Dependency injection is handled using Koin.

## Kotlin Multiplatform

The project uses Kotlin Multiplatform for shared application logic.

Most of the following code is located in `commonMain`:

- domain models
- repositories
- use cases
- ViewModels
- Compose UI
- networking
- dependency injection

Google Maps is implemented specifically for Android using an `expect` / `actual` abstraction.

The iOS implementation currently contains a placeholder because Google Maps rendering was outside the scope of the Android-focused assignment.

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
- Google Maps Compose
- Kotlin Test
- Ktor MockEngine

## Authentication

The application authenticates using the Deeper staging API.

After a successful login, the returned authentication token is used when requesting bathymetry data for individual scans.

Authentication errors are converted into user-friendly states such as:

- incorrect credentials
- access denied
- network failure
- server failure
- unknown error

## Bathymetry

Bathymetry data is requested only when the user opens a scan.

The returned geometry is converted from the API representation into domain models and displayed using Google Maps polygons.

Coordinates returned by the API are interpreted as:

```text
[longitude, latitude, depth]
```

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

The same depth configuration is used for both polygon rendering and the depth legend.

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

The login response does not provide reliable information indicating whether a scan has generated bathymetry.

For this reason, the application does not pre-fetch bathymetry for every scan.

The initial state is:

```text
UNKNOWN
```

After a scan has been loaded:

```text
geometry available → AVAILABLE
no geometry        → NOT_AVAILABLE
```

This information is then displayed when returning to the scan list.

## Caching

Successfully loaded bathymetry responses are cached in memory.

This prevents repeated network requests when reopening the same scan.

```text
First open
→ API request
→ map data cached

Second open
→ cached response
→ no additional API request
```

Failed requests are not cached so the user can retry them.

The cache currently exists for the lifetime of the repository instance and is not persisted to disk.

## Error Handling

Network results are converted into explicit application outcomes instead of exposing HTTP implementation details directly to the UI.

The application handles:

- unauthorized requests
- forbidden requests
- network errors
- server errors
- unknown errors

Coroutine cancellation is propagated instead of being converted into an application error.

The bathymetry screen provides a retry action when loading fails.

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

Repositories that maintain application state, such as the bathymetry cache, are registered as singleton instances.

## Testing

The project includes unit tests for the main application layers.

Coverage includes:

- API responses using Ktor MockEngine
- DTO-to-domain mapping
- repository behavior
- repository error handling
- bathymetry caching
- bathymetry availability
- Koin dependency configuration
- `LoginViewModel`
- `ScanListViewModel`
- `BathymetryViewModel`

Run the tests with:

```bash
./gradlew test
```

A complete project build can be run with:

```bash
./gradlew build
```

## Running the Application

### Requirements

- Android Studio
- Android device or emulator
- Android API 26 or newer
- Internet connection
- Google Maps API key

### Google Maps API Key

A Google Maps API key is required for the Android map implementation.

Create or update the `local.properties` file in the project root and add:

```properties
MAPS_API_KEY=YOUR_GOOGLE_MAPS_API_KEY
```

Then sync Gradle and run the Android application from Android Studio.

Do not commit `local.properties` or your actual API key to source control.

## Project Structure

```text
shared/
└── src/
    ├── commonMain/
    │   ├── kotlin/
    │   │   └── com.eligijus.deeper/
    │   │       ├── data/
    │   │       ├── domain/
    │   │       ├── presentation/
    │   │       └── di/
    │   └── composeResources/
    ├── androidMain/
    │   └── kotlin/
    │       └── Android-specific map implementation
    ├── iosMain/
    │   └── kotlin/
    │       └── iOS map placeholder
    └── commonTest/
        └── Unit tests
```

## Design Decisions

### Lazy bathymetry loading

Bathymetry responses may contain large amounts of geometry data.

Loading bathymetry for every scan immediately after login would create unnecessary network traffic and memory usage.

The application therefore loads bathymetry only when the user selects a scan.

### Domain models separate from DTOs

Network DTOs are converted into domain models before reaching the presentation layer.

This keeps the UI independent from the remote API representation and makes the application easier to test and maintain.

### Stateless screen composables

Where practical, screen UI is separated from dependency injection and ViewModel retrieval.

For example:

```text
ScanListRoute
    ↓
ScanListScreen

BathymetryRoute
    ↓
BathymetryScreen
```

The route owns the ViewModel and state collection while the screen remains focused on rendering UI.

This also allows screens to be previewed without starting Koin.

## Known Limitations

- Bathymetry cache is currently stored only in memory.
- Bathymetry availability is known only after a scan has been opened.
- Google Maps rendering is currently implemented only for Android.
- Scan availability is not pre-fetched to avoid downloading potentially large geometry responses.

## Author

Eligijus Kiudys
