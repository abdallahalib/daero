
## Context
The app needs to handle multiple states per screen including loading, error, and data states. The app also needs to handle one-time events like navigation and showing error messages. The app is offline-first which means the UI needs to react to local data changes automatically. The camera flow adds complexity because it has multiple states: idle, capturing, captured, and saved. All of this needed a clear and predictable way to manage state across screens.

## Decision
I used MVI architecture with a Repository pattern.

Each screen has:
- A UiState data class that represents everything the screen needs to render.
- A sealed Intent class that represents every action the user can take.
- A sealed Effect class that represents one-time events like navigation.
- A ViewModel that receives Intents, updates UiState, and emits Effects.
- A Composable that observes UiState and sends Intents to the ViewModel.

The Repository sits between the ViewModel and the data sources. ViewModels depend on the Repository interface defined in the domain layer, not the implementation.

## Alternatives Considered

### MVVM
MVVM is simpler. I considered it but decided against it because MVI gives a clearer structure for handling multiple states and one-time events which this app has a lot of.

### Clean Architecture with Use Cases
Adding a Use Cases layer between the ViewModel and the Repository would have made the architecture cleaner but added unnecessary complexity for a project of this scope. I decided to skip Use Cases and let the ViewModel call the Repository directly.

## Consequences
- Each screen has a clear and predictable state that is easy to understand and debug.
- One-time events like navigation are handled cleanly through the Effect channel without leaking into the UiState.
- Adding a new action to a screen requires adding a new Intent which makes it easy to track what actions are supported.

## Tradeoffs
- The Effect channel using a Kotlin Channel can miss events if the collector is not active. This is a known issue with Channel-based effects and was accepted as a tradeoff for this project scope.
- Skipping Use Cases keeps the codebase flat and easier to navigate but means the ViewModel is doing slightly more work .