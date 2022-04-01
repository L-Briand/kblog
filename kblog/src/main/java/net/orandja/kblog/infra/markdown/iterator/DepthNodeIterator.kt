package net.orandja.kblog.infra.markdown.iterator

import org.commonmark.node.Node

/** Create an iterator to navigate through a commonmark's [Node] */
class DepthNodeIterator(private val base: Node) : Iterator<DepthNodeIterator.Item> {

    data class Item(val depth: Int, val node: Node)

    private var current: Node = base
    private var next: Node? = null
    private var depth: Int = 0

    override fun hasNext(): Boolean = when {
        depth == 0 -> {
            next = current.firstChild
            depth++
            true
        }
        current.firstChild != null -> {
            next = current.firstChild
            depth++
            true
        }
        current.next != null -> {
            next = current.next
            true
        }
        else -> {
            getNextItemOnLowerDepth().also { (up, node) ->
                depth -= up
                next = node
            }
            this.next != null
        }
    }

    private fun getNextItemOnLowerDepth(from: Node = current, depth: Int = 1): Pair<Int, Node?> {
        if (from.parent == base) return depth to null
        return if (from.parent.next != null) depth to from.parent.next
        else getNextItemOnLowerDepth(from.parent, depth + 1)
    }

    override fun next(): Item = Item(depth, next!!).also { current = next!! }
}