package net.orandja.kblog.infra.endpoints

import com.charleskorn.kaml.Yaml
import com.soywiz.korte.AutoEscapeMode
import com.soywiz.korte.NewTemplateProvider
import com.soywiz.korte.TemplateConfig
import com.soywiz.korte.TemplateContent
import com.soywiz.korte.Templates
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.Serializable
import net.orandja.kblog._domain.IKtorModule
import net.orandja.kblog._domain.markdown.IMarkdownRenderer
import net.orandja.kblog._domain.resources.IResourceManager
import net.orandja.kblog.cases.noCatch

/** Delivers .md documents at root path */
class MarkdownDocument(
    private val markdownRenderer: IMarkdownRenderer,
    private val resourcesProvider: IResourceManager,
) : IKtorModule {

    // TODO : Make it a use-case
    @Serializable
    data class TemplateSelector(val template: String)

    // region TODO move or change for something better
    private val resourcesTemplateProvider = object : NewTemplateProvider {
        override suspend fun newGet(template: String): TemplateContent? =
            resourcesProvider.templateResource(template)
                ?.contentAsString()
                ?.let { TemplateContent(it) }
    }

    private val templates = Templates(
        resourcesTemplateProvider,
        config = TemplateConfig(autoEscapeMode = AutoEscapeMode.RAW),
        cache = false,
    )

    // endregion

    private suspend fun asHTML(documentId: String): String? {
        val resource = resourcesProvider.publicResource("$documentId.md") ?: return null
        val md = markdownRenderer.generate(resource.contentAsString())
        val template = markdownRenderer.getMetaDataHeader(md)
            ?.let { noCatch(null) { Yaml.default.decodeFromString(TemplateSelector.serializer(), it) } }
            ?.template?.ifBlank { null }
            ?: "default_template.html"

        return templates.get(template).invoke(
            "title" to markdownRenderer.getTitle(md),
            "content" to markdownRenderer.renderToHtml(md),
        )
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.notFound() {
        val result = asHTML("404") ?: ""
        call.respondText(ContentType.Text.Html, HttpStatusCode.NotFound) { result }
    }

    private fun Route.getDocument() {
        get("/") {
            val result = asHTML("index") ?: return@get notFound()
            call.respondText(ContentType.Text.Html) { result }
        }
        get("/{documentId}") {
            val documentId = call.parameters["documentId"] ?: return@get notFound()
            val result = asHTML(documentId) ?: return@get notFound()
            call.respondText(ContentType.Text.Html) { result }
        }
    }

    override fun Routing.route() {
        getDocument()
    }
}
