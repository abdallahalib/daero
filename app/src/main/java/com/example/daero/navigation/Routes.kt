package com.example.daero.navigation

import kotlinx.serialization.Serializable

@Serializable
object IssueListRoute

@Serializable
object NewIssueRoute

@Serializable
data class IssueDetailRoute(val issueId: String)

@Serializable
data class EditIssueRoute(val issueId: String)