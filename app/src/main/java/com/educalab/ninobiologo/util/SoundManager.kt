package com.educalab.ninobiologo.util

import android.media.AudioManager
import android.media.ToneGenerator

/**
 * Efectos de sonido muy breves para confirmar acciones (selección, acierto, error, desbloqueo).
 * Se usa ToneGenerator en vez de archivos de audio empaquetados: son tonos cortos generados por
 * el propio sistema Android, así la app no depende de assets binarios de audio y sigue siendo
 * 100% offline. Todo sonido puede silenciarse desde Ajustes (sección 12 de la especificación V3).
 */
class SoundManager {

    private var toneGenerator: ToneGenerator? = null
    var enabled: Boolean = true

    private fun generator(): ToneGenerator {
        var tg = toneGenerator
        if (tg == null) {
            tg = ToneGenerator(AudioManager.STREAM_MUSIC, 60) // volumen bajo y no intrusivo
            toneGenerator = tg
        }
        return tg
    }

    fun playSelect() = play(ToneGenerator.TONE_PROP_BEEP, 40)
    fun playCorrect() = play(ToneGenerator.TONE_PROP_ACK, 120)
    fun playIncorrect() = play(ToneGenerator.TONE_PROP_NACK, 120)
    fun playUnlock() = play(ToneGenerator.TONE_PROP_BEEP2, 180)

    private fun play(tone: Int, durationMs: Int) {
        if (!enabled) return
        runCatching { generator().startTone(tone, durationMs) }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
