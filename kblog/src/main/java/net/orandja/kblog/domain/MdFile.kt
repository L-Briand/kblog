package net.orandja.kblog.domain

import net.orandja.kblog.APP_CONFIG
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.relativeTo

object MdFile {

    private fun fetchDocumentFromDisk(): Sequence<Pair<String, Path>> {
        val basePath = APP_CONFIG.public.toPath()
        return basePath.toFile().walk().mapNotNull {
            if (!it.isFile) return@mapNotNull null
            if (!it.name.endsWith(".md")) return@mapNotNull null
            val path = it.toPath()
            val name = path.relativeTo(basePath).toString()
                .substringBefore(".") // name should be without extensions
                .sanitize()
            name to path
        }
    }

    /** It's not useful to re-fetch all articles from disk every time */
    private val lookup = mutableMapOf<String, Path>()
    private fun mdDocuments() = fetchDocumentFromDisk().onEach { lookup[it.first] = it.second }

    private fun mdDocumentPath(id: String): Path? {
        fun fromDisk() = mdDocuments().firstOrNull { it.first == id }?.second
        val path = lookup[id] ?: return fromDisk()
        return if (path.exists()) path else fromDisk() // article file can have moved
    }

    fun document(id: String): Document? {
        val file = mdDocumentPath(id)?.toFile() ?: return null
        if (!file.exists() || !file.isFile || !file.canRead()) return null
        return Document(id, Resource(file))
    }
}
