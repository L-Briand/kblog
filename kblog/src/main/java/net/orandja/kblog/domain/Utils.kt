package net.orandja.kblog.domain

import java.text.Normalizer
import kotlin.io.path.Path

val DIACRITICS_REGEX = "\\p{InCombiningDiacriticalMarks}+".toRegex()
val ALLOWED_CHARS = listOf('a'..'z', 'A'..'Z', '0'..'9').flatten()

fun String.removeDiacritics(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD) // make [è] > [e`], [œ] > [oe]
    return DIACRITICS_REGEX.replace(normalized, "") // remove [`]
}

fun String.sanitize(): String {
    val sanitized = removeDiacritics().map { if (ALLOWED_CHARS.contains(it)) it else '_' }
    return String(sanitized.toCharArray())
}

fun String.toPath() = Path(this)
