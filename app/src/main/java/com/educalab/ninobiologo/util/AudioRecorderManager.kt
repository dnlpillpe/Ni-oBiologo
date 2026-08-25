package com.educalab.ninobiologo.util

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import java.io.File
import java.io.IOException

/**
 * "Notas del biólogo": graba observaciones de voz con la API real de Android (MediaRecorder), sin
 * reconocimiento de voz y guardando el archivo únicamente en el almacenamiento privado de la app
 * (sección USO DE MICRÓFONO del prompt específico). No requiere permiso de almacenamiento porque
 * usa el directorio privado de archivos de la app.
 */
class AudioRecorderManager(private val context: Context) {

    private var recorder: MediaRecorder? = null
    private var player: MediaPlayer? = null
    private var currentFile: File? = null

    val audioDir: File get() = File(context.filesDir, "journal_audio").apply { mkdirs() }

    fun startRecording(): Result<File> = runCatching {
        stopPlayback()
        val file = File(audioDir, "nota_${System.currentTimeMillis()}.3gp")
        val mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }
        mediaRecorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        recorder = mediaRecorder
        currentFile = file
        file
    }

    fun stopRecording(): Result<File> = runCatching {
        val file = currentFile ?: throw IOException("No hay una grabación en curso")
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        currentFile = null
        file
    }

    fun cancelRecording() {
        runCatching {
            recorder?.stop()
            recorder?.release()
        }
        recorder = null
        currentFile?.delete()
        currentFile = null
    }

    fun playFile(path: String, onCompletion: () -> Unit) {
        stopPlayback()
        runCatching {
            player = MediaPlayer().apply {
                setDataSource(path)
                setOnCompletionListener { onCompletion() }
                prepare()
                start()
            }
        }
    }

    fun stopPlayback() {
        runCatching {
            player?.stop()
            player?.release()
        }
        player = null
    }

    fun deleteFile(path: String) {
        runCatching { File(path).delete() }
    }
}
