package net.orandja.kblog.infra.markdown

import net.orandja.kblog._domain.markdown.IMarkdownRenderer
import net.orandja.kblog._domain.markdown.IMarkdownRenderer.Markdown
import net.orandja.kblog.infra.markdown.extensions.LinkerModifier
import net.orandja.kblog.infra.markdown.extensions.MetaDataBlock
import net.orandja.kblog.infra.markdown.extensions.MetaDataParserExtension
import net.orandja.kblog.infra.markdown.iterator.NodeIterator
import org.commonmark.Extension
import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension
import org.commonmark.ext.image.attributes.ImageAttributesExtension
import org.commonmark.ext.ins.InsExtension
import org.commonmark.ext.task.list.items.TaskListItemsExtension
import org.commonmark.node.Heading
import org.commonmark.node.Node
import org.commonmark.node.Text
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class MarkdownRenderer : IMarkdownRenderer {
    private val extensions = listOf<Extension>(
        AutolinkExtension.create(),
        StrikethroughExtension.create(),
        TablesExtension.create(),
        HeadingAnchorExtension.create(),
        InsExtension.create(),
        ImageAttributesExtension.create(),
        TaskListItemsExtension.create(),

        LinkerModifier,
        MetaDataParserExtension,
    )

    private val parser = Parser.builder().extensions(extensions).build()
    private val renderer = HtmlRenderer.builder().extensions(extensions).build()

    private data class Holder(val document: Node) : Markdown

    override fun generate(rawMarkdown: String): Markdown = Holder(parser.parse(rawMarkdown))

    override fun getTitle(markdown: Markdown): String? {
        if (markdown !is Holder) throw IllegalStateException("Markdown not generated correctly")
        return Iterable { NodeIterator(markdown.document) }
            .firstOrNull { it is Heading }
            ?.let { Iterable { NodeIterator(it) } }
            ?.joinToString("", "", "") { if (it is Text) it.literal else "" }
    }

    override fun getMetaDataHeader(markdown: Markdown): String? {
        if (markdown !is Holder) throw IllegalStateException("Markdown not generated correctly")
        val yamlBlock = markdown.document.firstChild
        if (yamlBlock !is MetaDataBlock) return null
        return yamlBlock.rawYaml
    }

    override fun renderToHtml(markdown: Markdown): String {
        if (markdown !is Holder) throw IllegalStateException("Markdown not generated correctly")
        return renderer.render(markdown.document)
    }
}
