package net.orandja.kblog.infra

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default
import net.orandja.kblog._domain.IConfig

class Config(parser: ArgParser) : IConfig {

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
