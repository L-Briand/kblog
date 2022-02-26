package net.orandja.kblog.domain

import net.orandja.kblog.APP_CONFIG
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.relativeTo

data class Article(val name: String, val resource: Resource)

object Articles {

    /**
     * All files that ends with `APP_CONFIG.extension` are considered articles.
     * It fetches all of them inside `APP_CONFIG.articles`
     */
    private fun fetchArticlesFromDisk(): Sequence<Pair<String, Path>> {
        val basePath = APP_CONFIG.public.toPath()
        return basePath.toFile().walk().mapNotNull {
            if (!it.isFile) return@mapNotNull null
            if (!it.name.endsWith(APP_CONFIG.extension)) return@mapNotNull null
            val path = it.toPath()
            val name = path.relativeTo(basePath).toString()
                .substringBefore(APP_CONFIG.extension).sanitize()
            name to path
        }
    }

    /** It's not useful to re-fetch all articles from disk every time */
    private val lookup = mutableMapOf<String, Path>()
    private fun articles() = fetchArticlesFromDisk().onEach { lookup[it.first] = it.second }
    private fun articlePath(id: String): Path? {
        fun fromDisk() = articles().firstOrNull { it.first == id }?.second
        val path = lookup[id] ?: return fromDisk()
        return if (path.exists()) path else fromDisk() // article file can have moved
    }

    fun article(id: String): Article? {
        val file = articlePath(id)?.toFile() ?: return null
        if (!file.exists() || !file.isFile || !file.canRead()) return null
        return Article(id, Resource(file))
    }
}
