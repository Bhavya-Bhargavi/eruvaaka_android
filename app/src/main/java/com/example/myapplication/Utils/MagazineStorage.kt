package com.example.myapplication.Utils

import android.content.Context
import android.os.Environment
import java.io.File

class MagazineStorage(
    private val context: Context
) {

    private val magazineDirectory: File
        get() = File(context.filesDir,
            //context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "magazines"
        )

    fun getMagazineFile(fileName: String): File {

        if (!magazineDirectory.exists()) {
            magazineDirectory.mkdirs()
        }

        return File(magazineDirectory, fileName)
    }

    fun isMagazineDownloaded(fileName: String): Boolean {
        return getMagazineFile(fileName).exists()
    }
}