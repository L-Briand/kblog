package net.orandja.kblog.infra.markdown.iterator

import org.commonmark.node.Node

class NodeIterator(base: Node) : Iterator<Node> {
    private val backing = DepthNodeIterator(base)
    override fun hasNext(): Boolean = backing.hasNext()
    override fun next(): Node = backing.next().node
}
