package net.orandja.kblog._domain.resources

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

interface IResource {
    fun name(): String?
    fun content(): InputStream
    suspend fun contentAsBytes(): ByteArray = withContext(Dispatchers.IO) { content().use { it.readAllBytes() } }
    suspend fun contentAsString(): String = String(contentAsBytes(), Charsets.UTF_8)
}