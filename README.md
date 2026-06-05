Daero Field Issue Tracker
## Setup Instructions
1- Clone the repository
2- Open the project in Android Studio
3- Sync the project with Gradle files
4- Run the app on a device or emulator with a camera

## How to Run the App
1- Connect a physical device or start an emulator
2- Click the Run button in Android Studio## How to Run Tests
Instrumented tests:
```
./gradlew connectedAndroidTest
```

## Architecture Summary
The app is built using MVI architecture. The app is built using the following technologies:
- Jetpack Compose
- Room
- WorkManager for background sync
- Koin for dependency injection
- CameraX Jetpack Compose


Koin was used for dependency injection because it reduced the time and code of wiring dependencies across the app. It made injecting the Repository into ViewModels and the WorkManager/DAO/FakeRemoteService into the Repository faster without writing manual factory classes for each.

The app is structured into three layers:
- Presentation layer: Composables observe UiState from ViewModels and send Intents to trigger actions. ViewModels emit Effects for one-time events like navigation.
- Domain layer: Models and Repository interfaces
- Data layer: Room database, DAOs, repository implementations, and RemoteService

## Completed Features
1- Issues list is loaded and displayed in the UI and updated automatically when a new issue is added.
2- Issues list DB is ready for loading the issues list asynchronously, inserting new issues, updating issues, and changing sync status.
3- New issue flow.
4- Partially new issue completion saved as draft
5- Sync button.
6- Issue detail UI is completed for displaying issue details.
7- Editing saved issues.
8- Storing remote id for synced issues.

## Notes
1- The sync notification is not shown on the first sync attempt, it works from the second time.
2- The app does not support multiple photos per issue.
3- The image are stored in app internal files, so a image cleaning mechanism is required to remove the captured unused images (e.g if the user captured an image and then click on retake). We can setup a work manager every day to check if there are image doesn't linked to our room db and remove them.
1- I used WorkManager for background sync to make sure the sync process will work even if the app is closed.
3- Draft state is handled by marking the issue as a draft in the database. If the user closes the app before saving the issue, the issue will be saved as a draft and the user can continue editing it when the app is reopened.
4- I used FileProvider and internal app storage for storing images instead of MediaStore. This means the images are private to the app and will be deleted when the app is uninstalled. It is a work app, so I didn't think that storing the images in device's gallery using a MediaStore a smart move.

## What I Would Improve Next
1- Fix the sync notification not showing on the first sync attempt.
2- Add retry behavior for failed sync attempts.
3- Add background sync that triggers automatically when connectivity is restored.

## Branches
There is 3 branches.
1- main (db_v3).
2- db_v1, shows the app state before implementing draft, and syncing.
2- db_v2, shows the app state after implementing draft and syncing but without storing the remote id.
You can run db_v1, then db_v2, then main to test room mitigation.
