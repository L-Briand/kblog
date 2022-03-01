package net.orandja.kblog.mods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import net.orandja.kblog.KtorModule
import net.orandja.kblog.domain.MdFile
import net.orandja.kblog.domain.Resources
import net.orandja.kblog.domain.renderMarkdownToHtml

/** Delivers .md documents at root path */
object MdDocuments : KtorModule {

    private suspend fun asHTML(documentId: String): String? {
        val document = MdFile.document(documentId) ?: return null
        val template = document.extensions.firstNotNullOfOrNull { Resources.template("$it.html") } ?: return null
        val mdRender = renderMarkdownToHtml(document.resource.contentAsString())
        return template(
            "title" to mdRender.title,
            "content" to mdRender.renderedDocument,
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

    override fun Routing.module() {
        getDocument()
    }
}
