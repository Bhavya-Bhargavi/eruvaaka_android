package com.example.myapplication.Utils

import android.content.Context
import android.graphics.BitmapFactory
import java.io.File

class MagazineStorage(
    private val context: Context
) {

    private val magazineDirectory: File
        get() = File(context.filesDir, "magazines")

    fun getMagazineFile(fileName: String): File {
        if (!magazineDirectory.exists()) {
            magazineDirectory.mkdirs()
        }
        return File(magazineDirectory, fileName)
    }

    fun isValidDocument(file: File): Boolean {
        if (!file.exists() || file.length() < 10) return false

        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(file.absolutePath, options)
        if (options.outWidth > 0 && options.outHeight > 0) {
            return true
        }

        return try {
            file.inputStream().use { stream ->
                val header = ByteArray(5)
                val count = stream.read(header)
                if (count == 5) {
                    val headerStr = String(header, Charsets.US_ASCII)
                    headerStr == "%PDF-"
                } else false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun isMagazineDownloaded(fileName: String): Boolean {
        val file = getMagazineFile(fileName)
        if (!file.exists()) return false
        if (!isValidDocument(file)) {
            file.delete()
            return false
        }
        return true
    }
}
