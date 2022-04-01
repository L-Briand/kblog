package net.orandja.kblog.infra.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.routing.*
import net.orandja.kblog._domain.IConfig
import net.orandja.kblog._domain.IKtorModule
import net.orandja.kblog.cases.toPath
import org.koin.core.component.KoinComponent
import java.time.ZonedDateTime

class ResourcesProvider(
    private val config: IConfig
) : IKtorModule, KoinComponent {
    override fun Routing.route() {
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
            staticRootFolder = config.resources.toPath().toFile()
            files("public")
        }
    }
}
