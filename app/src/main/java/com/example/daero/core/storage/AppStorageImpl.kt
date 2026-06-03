package com.example.daero.core.storage

import android.content.Context
import java.io.File

class AppStorageImpl(
    private val context: Context,
) : AppStorage {
    override fun createImageFile(): File {
        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
        return File(imagesDir, "daero_${System.currentTimeMillis()}.jpg")
    }
}

