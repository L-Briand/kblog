package net.orandja.kblog.infra.markdown.extensions

import org.commonmark.internal.DocumentBlockParser
import org.commonmark.node.Block
import org.commonmark.node.CustomBlock
import org.commonmark.parser.Parser
import org.commonmark.parser.Parser.ParserExtension
import org.commonmark.parser.block.AbstractBlockParser
import org.commonmark.parser.block.AbstractBlockParserFactory
import org.commonmark.parser.block.BlockContinue
import org.commonmark.parser.block.BlockStart
import org.commonmark.parser.block.MatchedBlockParser
import org.commonmark.parser.block.ParserState

class MetaDataBlock(var rawYaml: String? = null) : CustomBlock()

object MetaDataParserExtension : ParserExtension {
    override fun extend(parserBuilder: Parser.Builder?) {
        parserBuilder?.customBlockParserFactory(MetaDataBPF())
    }

    class MetaDataBPF : AbstractBlockParserFactory() {
        override fun tryStart(state: ParserState, matchedBlockParser: MatchedBlockParser): BlockStart? {
            val line = state.line.content
            val parentParser = matchedBlockParser.matchedBlockParser
            return if (parentParser is DocumentBlockParser && parentParser.block.firstChild == null && line.startsWith("---"))
                BlockStart.of(MetaDataBP()).atIndex(state.nextNonSpaceIndex)
            else
                BlockStart.none()
        }
    }

    class MetaDataBP : AbstractBlockParser() {
        private var _block = MetaDataBlock()
        private val rawYaml = StringBuilder()
        override fun getBlock(): Block = _block

        override fun tryContinue(parserState: ParserState?): BlockContinue {
            parserState ?: return BlockContinue.finished()
            val line = parserState.line.content
            if (line.startsWith("---")) {
                _block.rawYaml = rawYaml.toString()
                return BlockContinue.finished()
            }
            rawYaml.append(line)
            return BlockContinue.atIndex(parserState.index)
        }
    }
}