package net.orandja.kblog.infra.markdown.extensions

import org.commonmark.internal.DocumentBlockParser
import org.commonmark.node.Block
import org.commonmark.node.CustomBlock
import org.commonmark.parser.Parser
import org.commonmark.parser.Parser.ParserExtension
import org.commonmark.parser.block.*

class MetaDataBlock(val rawYaml: String) : CustomBlock()

object MetaDataParserExtension : ParserExtension {
    override fun extend(parserBuilder: Parser.Builder?) {
        parserBuilder?.customBlockParserFactory(MetaDataBPF())
    }

    class MetaDataBPF : BlockParserFactory {
        override fun tryStart(state: ParserState, matchedBlockParser: MatchedBlockParser): BlockStart {
            val line = state.line.content
            val parentParser = matchedBlockParser.matchedBlockParser
            if (parentParser is DocumentBlockParser && parentParser.block.firstChild == null && line.startsWith("---"))
                MetaDataBP()
            return BlockStart.none()
        }
    }

    class MetaDataBP : AbstractBlockParser() {
        private lateinit var _block: Block
        private val rawYaml = StringBuilder()
        override fun getBlock(): Block = _block

        override fun tryContinue(parserState: ParserState?): BlockContinue {
            parserState ?: return BlockContinue.finished()
            val line = parserState.line.content
            if (line.startsWith("---")) {
                _block = MetaDataBlock(rawYaml.toString())
                return BlockContinue.finished()
            }
            rawYaml.append(line)
            return BlockContinue.atIndex(parserState.index)
        }
    }
}