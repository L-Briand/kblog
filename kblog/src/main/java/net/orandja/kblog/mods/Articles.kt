package net.orandja.kblog.mods

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import net.orandja.kblog.KtorModule
import net.orandja.kblog.domain.Articles
import net.orandja.kblog.domain.Resources
import net.orandja.kblog.domain.renderMarkdownToHtml

/** Delivers .art.md articles at root */
object Articles : KtorModule {

    private suspend fun asHTML(articleId: String): String? {
        val article = Articles.article(articleId) ?: return null
        val mdRender = renderMarkdownToHtml(article.resource.contentAsString())
        return Resources.template("article.html")(
            "title" to mdRender.title,
            "content" to mdRender.renderedDocument,
        )
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.notFound() {
        val result = asHTML("404") ?: ""
        call.respondText(ContentType.Text.Html, HttpStatusCode.NotFound) { result }
    }

    private fun Route.getArticle() {
        get("/") {
            val result = asHTML("index") ?: return@get notFound()
            call.respondText(ContentType.Text.Html) { result }
        }
        get("/{articleId}") {
            val articleId = call.parameters["articleId"] ?: return@get notFound()
            val result = asHTML(articleId) ?: return@get notFound()
            call.respondText(ContentType.Text.Html) { result }
        }
    }

    override fun Routing.module() {
        getArticle()
    }
}
