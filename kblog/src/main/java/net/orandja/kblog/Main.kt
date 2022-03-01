package net.orandja.kblog

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.orandja.kblog.mods.MdDocuments
import net.orandja.kblog.mods.ResourcesProvider

/** All available apis are registered here. Order is important */
val MODULES = listOf(
    MdDocuments,
    ResourcesProvider,
)

fun main(args: Array<String>) {
    APP_CONFIG = mainBody { ArgParser(args).parseInto(Config::Cli) }
    validateConfigPathsOrExit()

    embeddedServer(Netty, port = APP_CONFIG.port.toInt()) {
        MODULES.onEach { it.module(this) }
    }.start(true)
}
