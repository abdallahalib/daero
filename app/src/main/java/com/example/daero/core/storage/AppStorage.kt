package com.example.daero.core.storage

import java.io.File

interface AppStorage {
    fun createImageFile(): File

    fun deleteImageFile(filePath: String): Boolean
}

