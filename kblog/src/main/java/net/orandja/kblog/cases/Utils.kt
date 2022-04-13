package net.orandja.kblog.cases

import kotlin.io.path.Path

fun String.toPath() = Path(this)

inline fun <reified T> noCatch(default: T, block: () -> T): T = try {
    block()
} catch (e: Exception) {
    default
}