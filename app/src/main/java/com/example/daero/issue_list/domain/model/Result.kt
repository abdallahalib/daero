package com.example.daero.issue_list.domain.model

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable, val message: String) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}