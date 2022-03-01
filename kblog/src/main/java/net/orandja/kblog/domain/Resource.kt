package net.orandja.kblog.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class Document(val name: String, val resource: Resource) {

    /** for a file like "my.awesome.file.md" it retrieves "awesome.file.md", "file.md" and "md" */
    val extensions: List<String> by lazy {
        val extensions = mutableListOf<String>()
        var lastExtension = resource.file.name
        for (count in 0 until lastExtension.count { it.equals('.') }) {
            lastExtension = lastExtension.substringAfter('.')
            extensions += lastExtension
        }
        extensions
    }
}

data class Resource(val file: File) {
    suspend fun contentAsString() = withContext(Dispatchers.IO) {
        file.inputStream().use { String(it.readAllBytes(), Charsets.UTF_8) }
    }
}
