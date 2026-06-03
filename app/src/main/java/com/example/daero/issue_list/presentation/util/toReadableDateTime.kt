package com.example.daero.issue_list.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toReadableDateTime(): String {
    return SimpleDateFormat("EEEE MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(this))
}