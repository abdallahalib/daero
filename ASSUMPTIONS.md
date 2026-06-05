# Assumptions

## Sync Behavior
1- Sync is triggered manually by the user using sync button in the top app bar.
2- The sync process runs in the background using WorkManager, so it will continue even if the app is closed.
3- Each issue is synced individually, if one issue fails to sync it will be marked as failed and the rest of the issues will continue syncing.
4- The fake remote service simulates a 5 seconds delay to simulate a real network request.
5- All issues will be marked as synced, failed, conflict based on `issueSyncStatus` in `FakeRemoteService`

```
private val issueSyncStatus = IssueSyncStatus.SYNCED
```
## Conflict Representation
1- The conflict state is represented clearly in the issues list and the issue detail screen.
2- The user can still edit a conflicted issue and sync it again.

## Photo-First Capture
1- The user must capture an image before creating an issue, you can not create an issue without an image.
3- After the image is captured and confirmed, the user is taken to the issue details screen to fill in the issue details.

## Draft Photo State
1- A draft is created when the user captures and confirms a photo without completing the details.
2- If the user cancels after capturing a photo without confirming the captured photo, no draft will be saved.
3- On next launch the app will show the draft issue and the user can continue editing it.

## Local Photo Persistence
1- Photos are stored in the app internal storage using FileProvider.
2- Photos are private to the app and will not appear in the device gallery.
3- Photos will be deleted when the app is uninstalled.
4- Room stores the absolute file path of the photo.
## Intentionally Not Built
- Background sync
- Multiple photos per issue
- Delete issue while offline
- Retry behavior for failed sync
- Debug screen for sync state
- Pulling remote changes into the local app
- Compose UI test for a critical path
- Multi-module project organization
- Sync history or audit trail
- Basic image compression or thumbnail generation