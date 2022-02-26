package net.orandja.kblog.domain

import com.soywiz.korte.*
import io.ktor.utils.io.errors.*
import net.orandja.kblog.APP_CONFIG
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

object Resources {
    /** Get file inside [APP_CONFIG.resources] folder */
    fun getResource(resourceName: String): Resource? {
        // check for queried file
        val resourceFilePath = Path(APP_CONFIG.resources.toPath().toString(), resourceName)
        if (!resourceFilePath.exists() || !resourceFilePath.isRegularFile()) return null

        return try {
            Resource(resourceFilePath.toFile())
        } catch (e: IOException) {
            null
        }
    }

    private val resourcesTemplateProvider = object : NewTemplateProvider {
        override suspend fun newGet(template: String): TemplateContent? =
            getResource("/templates/$template")?.contentAsString()?.let { TemplateContent(it) }
    }
    private val templates =
        Templates(resourcesTemplateProvider, config = TemplateConfig(autoEscapeMode = AutoEscapeMode.RAW))

    /** Get a [Template] from inside [Appconfig.resources]/templates/ folder */
    suspend fun template(name: String) = templates.get(name)
}
