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

    override fun deleteImageFile(filePath: String): Boolean {
        val file = File(filePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}

