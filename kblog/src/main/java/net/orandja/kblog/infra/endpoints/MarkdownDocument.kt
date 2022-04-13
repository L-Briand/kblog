package net.orandja.kblog.infra.endpoints

import com.soywiz.korte.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import net.orandja.kblog._domain.IKtorModule
import net.orandja.kblog._domain.markdown.IMarkdownRenderer
import net.orandja.kblog._domain.resources.IResourceManager

/** Delivers .md documents at root path */
class MarkdownDocument(
    private val markdownRenderer: IMarkdownRenderer,
    private val resourcesProvider: IResourceManager,
) : IKtorModule {

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

    private suspend fun asHTML(documentId: String): String? {
        val resource = resourcesProvider.publicResource("$documentId.md") ?: return null
        val md = markdownRenderer.generate(resource.contentAsString())
        println("HEADERS : ")
        println(markdownRenderer.getMetaDataHeader(md))
        return templates.get("default_template.html").invoke(
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
