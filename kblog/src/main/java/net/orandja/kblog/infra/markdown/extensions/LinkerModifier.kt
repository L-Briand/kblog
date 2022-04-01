package net.orandja.kblog.infra.markdown.extensions

import net.orandja.kblog.infra.markdown.iterator.NodeIterator
import org.commonmark.node.Link
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.parser.PostProcessor
import java.net.URI

object LinkerModifier : Parser.ParserExtension {
    override fun extend(parserBuilder: Parser.Builder?) {
        parserBuilder!!.postProcessor(LinkerProcessor())
    }

    private class LinkerProcessor : PostProcessor {
        override fun process(node: Node?): Node? = node?.also {
            for (inNode in NodeIterator(node)) {
                if (inNode !is Link) continue
                if (URI(inNode.destination).scheme != null) continue
                inNode.destination = inNode.destination.removeSuffix(".md")
            }
        }
    }
}
