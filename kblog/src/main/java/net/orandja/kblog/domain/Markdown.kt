package net.orandja.kblog.domain

import org.commonmark.ext.autolink.AutolinkExtension
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
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

private val extensions = listOf(
    AutolinkExtension.create(),
    StrikethroughExtension.create(),
    TablesExtension.create(),
    HeadingAnchorExtension.create(),
    InsExtension.create(),
    YamlFrontMatterExtension.create(),
    ImageAttributesExtension.create(),
    TaskListItemsExtension.create(),
)

private val parser = Parser.builder().extensions(extensions).build()
private val renderer = HtmlRenderer.builder().extensions(extensions).build()

/** Create an iterator to navigate through a tree like graph node */
class TreeNodeIterator(val base: Node) : Iterator<Node> {
    private var visited: Node? = null
    private var current: Node = base

    override fun hasNext(): Boolean =
        visited != current.firstChild && current.firstChild != null ||
            current.next != null && current != base ||
            current.parent != null && current != base

    override fun next(): Node = (
        if (visited != current.firstChild && current.firstChild != null) current.firstChild else null
            ?: current.next
            ?: run {
                current.parent?.firstChild?.also { visited = it }
                current.parent
            }
        ).also { current = it }
}

data class MDRender(val title: String, val renderedDocument: String)

fun getTitle(document: Node): String {
    val title = StringBuilder()
    for (node in TreeNodeIterator(document)) {
        if (node is Heading && node.level == 1) {
            TreeNodeIterator(node).forEach {
                if (it is Text) title.append(it.literal)
            }
            break
        }
    }
    return title.toString()
}

fun renderMarkdownToHtml(markdown: String) = parser.parse(markdown).let {
    MDRender(getTitle(it), renderer.render(it))
}
