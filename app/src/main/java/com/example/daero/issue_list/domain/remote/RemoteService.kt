package com.example.daero.issue_list.domain.remote

import com.example.daero.issue_list.domain.model.Issue
import com.example.daero.issue_list.domain.model.Result

interface RemoteService {
    suspend fun createIssue(issue: Issue): Issue
    suspend fun updateIssue(issue: Issue): Issue
    suspend fun uploadPhoto(issueId: String, photoPath: String): Result<Unit>
}