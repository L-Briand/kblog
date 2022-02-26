package net.orandja.kblog.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class Resource(val file: File) {
    suspend fun contentAsString() = withContext(Dispatchers.IO) {
        file.inputStream().use { String(it.readAllBytes(), Charsets.UTF_8) }
    }
}
