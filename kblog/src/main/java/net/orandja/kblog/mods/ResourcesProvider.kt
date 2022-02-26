package net.orandja.kblog.mods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import net.orandja.kblog.APP_CONFIG
import net.orandja.kblog.KtorModule
import net.orandja.kblog.domain.toPath
import java.time.ZonedDateTime

object ResourcesProvider : KtorModule {
    override fun Routing.module() {
        install(Compression)
        install(CachingHeaders) {
            options { outgoingContent ->
                when (outgoingContent.contentType?.withoutParameters()) {
                    ContentType.Text.CSS,
                    ContentType.Application.JavaScript,
                    ContentType.Image.XIcon,
                    ContentType.Image.PNG,
                    ContentType.Image.JPEG,
                    -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 3600), ZonedDateTime.now().plusHours(1))
                    else -> null
                }
            }
        }
        static("public") {
            staticRootFolder = APP_CONFIG.resources.toPath().toFile()
            files("public")
        }
    }
}
