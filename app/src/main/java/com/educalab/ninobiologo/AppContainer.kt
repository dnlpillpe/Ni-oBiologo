package com.educalab.ninobiologo

import android.content.Context
import com.educalab.ninobiologo.data.local.AppDatabase
import com.educalab.ninobiologo.data.repository.BiologyRepository
import com.educalab.ninobiologo.util.AudioRecorderManager
import com.educalab.ninobiologo.util.PhotoStorageManager
import com.educalab.ninobiologo.util.SoundManager

/**
 * Contenedor manual de dependencias (sección 6 de la especificación maestra: se prioriza
 * simplicidad y estabilidad sobre introducir Hilt). Vive en la Application y es compartido por
 * toda la app a través de NinoBiologoApp.
 */
class AppContainer(context: Context) {
    private val database: AppDatabase = AppDatabase.getInstance(context)

    val repository: BiologyRepository = BiologyRepository(database)
    val soundManager: SoundManager = SoundManager()
    val audioRecorderManager: AudioRecorderManager = AudioRecorderManager(context)
    val photoStorageManager: PhotoStorageManager = PhotoStorageManager(context)
}
