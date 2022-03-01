package net.orandja.kblog

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.system.exitProcess

/** configuration for all app */
lateinit var APP_CONFIG: Config

interface Config {
    val port: Short
    val listen: String
    val resources: String

    class Cli(parser: ArgParser) : Config {
        override val port: Short by parser
            .storing("-p", "--port", help = "used port") { toShortOrNull() ?: 8080 }
            .default(8080)

        override val listen: String by parser
            .storing("-l", "--listen", help = "listen to address") { if (isNullOrBlank()) "0.0.0.0" else this }
            .default("0.0.0.0")

        override val resources: String by parser
            .storing("-d", "--directory", help = "root folder used to deliver content")
            .default("blog")
    }

    val public: String get() = "$resources${File.separator}public"
    val templates: String get() = "$resources${File.separator}templates"
}

fun validateConfigPathsOrExit() {
    listOf(
        Path(APP_CONFIG.resources),
        Path(APP_CONFIG.public),
        Path(APP_CONFIG.templates),
    ).forEach {
        if (!it.exists()) {
            System.err.println("Directory not found (${it.absolutePathString()})")
            exitProcess(1)
        }
        if (!it.isDirectory()) {
            System.err.println("${it.absolutePathString()} should be a directory")
            exitProcess(1)
        }
    }
}
