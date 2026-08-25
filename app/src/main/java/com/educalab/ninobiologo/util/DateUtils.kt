package com.educalab.ninobiologo.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val displayFormat = SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "ES"))
    private val shortFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "ES"))

    fun formatDisplay(epochMillis: Long): String = displayFormat.format(Date(epochMillis))
    fun formatShort(epochMillis: Long): String = shortFormat.format(Date(epochMillis))
}
