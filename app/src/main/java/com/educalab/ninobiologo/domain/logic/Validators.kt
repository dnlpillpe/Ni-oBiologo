package com.educalab.ninobiologo.domain.logic

/** Validaciones y utilidades pequeñas reutilizadas por varios motores y por los ViewModels. */
object Validators {

    private const val MAX_ALIAS_LENGTH = 18
    private const val MAX_JOURNAL_NOTE_LENGTH = 500

    /** Recorta y limpia el alias del explorador. Nunca se pide nombre real (privacidad infantil). */
    fun sanitizeAlias(rawAlias: String): String {
        val trimmed = rawAlias.trim().replace(Regex("\\s+"), " ")
        return if (trimmed.length > MAX_ALIAS_LENGTH) trimmed.take(MAX_ALIAS_LENGTH) else trimmed
    }

    fun isAliasValid(alias: String): Boolean = alias.trim().isNotEmpty() && alias.trim().length <= MAX_ALIAS_LENGTH

    fun isJournalNoteValid(note: String): Boolean = note.length <= MAX_JOURNAL_NOTE_LENGTH

    fun sanitizeJournalNote(rawNote: String): String =
        if (rawNote.length > MAX_JOURNAL_NOTE_LENGTH) rawNote.take(MAX_JOURNAL_NOTE_LENGTH) else rawNote

    fun hasDuplicateIds(ids: List<String>): Boolean = ids.size != ids.distinct().size

    /** División segura usada por los motores de puntuación para evitar división entre cero. */
    fun safeRatio(numerator: Int, denominator: Int): Float =
        if (denominator == 0) 0f else numerator.toFloat() / denominator.toFloat()

    fun clampPercent(value: Int): Int = value.coerceIn(0, 100)
}
