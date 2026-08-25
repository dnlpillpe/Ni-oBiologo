package com.educalab.ninobiologo.util

import android.content.Context
import androidx.core.content.FileProvider
import android.net.Uri
import java.io.File

/**
 * "Diario del explorador": las fotografías se guardan en el almacenamiento privado de la app
 * (sección USO DE CÁMARA del prompt específico: sin reconocimiento automático ni identificación
 * de especies por IA, solo un registro visual local del niño).
 */
class PhotoStorageManager(private val context: Context) {

    val photoDir: File get() = File(context.filesDir, "journal_photos").apply { mkdirs() }

    fun createNewPhotoFile(): File = File(photoDir, "foto_${System.currentTimeMillis()}.jpg")

    fun uriForFile(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    fun deletePhoto(path: String) {
        runCatching { File(path).delete() }
    }
}
