# AI Usage

## Overview
Claude Code is used throughout this project as a technical advisor and pair programmer. All AI-generated code and documentation was reviewed, understood, and in most cases modified before being used in the project.

## Claude Code Usage
Claude Code was used to create the initial file structure for the fake remote service layer including:
- `RemoteService.kt` interface
- `RemoteResult.kt` sealed class
- `FakeSyncBehavior.kt` enum
- `FakeRemoteService.kt` implementation

After Claude Code created these files, I reviewed the code, modified it to match my project structure and naming conventions, and wired it into the existing codebase manually.

## Where I Did Not Trust Claude Code
- The generated code was using a per-issue override map for the fake service. I simplified it to a single behavior flag because it matched my use case and was easier to understand.
- The generated RemoteResult was returning a remoteId which I had not implemented yet. I modified it to return a domain Issue instead.
## PROMPT

Create the following files in the Android project at /Users/abdallahali/AndroidStudioProjects/Daero on the main branch.

1. Create file: app/src/main/java/com/example/daero/domain/remote/RemoteService.kt
interface RemoteService with three suspend functions: createIssue(issue: Issue): RemoteResult, updateIssue(issue: Issue): RemoteResult, uploadPhoto(issueId: String, photoPath: String): RemoteResult

2. Create file: app/src/main/java/com/example/daero/data/remote/RemoteResult.kt
sealed class RemoteResult with three subclasses:
- Success(remoteId: String, remoteUpdatedAt: Long)
- NetworkFailure (data object)
- Conflict(remoteUpdatedAt: Long)

3. Create file: app/src/main/java/com/example/daero/data/remote/FakeSyncBehavior.kt
enum class FakeSyncBehavior with values: SUCCESS, NETWORK_FAILURE, CONFLICT

4. Create file: app/src/main/java/com/example/daero/data/remote/FakeRemoteService.kt
class FakeRemoteService(private val delayMs: Long = 10_000L) implementing RemoteService with:
- var behavior: FakeSyncBehavior = SUCCESS
- private val overrides = ConcurrentHashMap<String, FakeSyncBehavior>()
- fun setOverride(issueId: String, behavior: FakeSyncBehavior)
- fun clearOverride(issueId: String)
- fun clearAllOverrides()
- override suspend fun createIssue — delay then resolveResult with generated remoteId
- override suspend fun updateIssue — delay then resolveResult preserving existing remoteId
- override suspend fun uploadPhoto — delay then resolveResult with photo remoteId
- private fun resolveResult(id: String, remoteId: String): RemoteResult using overrides map then global behavior

Use package com.example.daero.data.remote for data classes and com.example.daero.domain.remote for the interface.
Import Issue from com.example.daero.domain.model.Issue.

After creating all files, stage and commit to main branch with message: "add fake remote service with interface abstraction and per-issue behavior overrides"