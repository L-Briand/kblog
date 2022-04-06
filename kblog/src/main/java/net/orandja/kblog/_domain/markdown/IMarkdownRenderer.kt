package net.orandja.kblog._domain.markdown

interface IMarkdownRenderer {
    fun generate(rawMarkdown: String): Markdown
    fun getTitle(markdown: Markdown): String?
    fun renderToHtml(markdown: Markdown): String

    fun renderToHtml(rawMarkdown: String): String = renderToHtml(generate(rawMarkdown))

    interface Markdown
}
