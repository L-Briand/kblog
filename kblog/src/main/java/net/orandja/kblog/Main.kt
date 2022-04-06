package net.orandja.kblog

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import kotlinx.coroutines.runBlocking
import net.orandja.kblog._domain.IConfig
import net.orandja.kblog._domain.endpoints.IEndpoints
import net.orandja.kblog.cases.KoinModuleCases
import net.orandja.kblog.cases.PathValidation
import net.orandja.kblog.infra.Config
import net.orandja.kblog.infra.KoinModuleInfra
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class Main : KoinComponent {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) = Main().start(args)
    }

    fun start(args: Array<String>) = runBlocking {
        val config = mainBody { ArgParser(args).parseInto(::Config) }
        val configMod = module { single<IConfig> { config } }
        startKoin {
            modules(configMod)
            modules(KoinModuleCases.modules)
            modules(KoinModuleInfra.modules)
        }

        get<PathValidation>().validateConfigPathsOrExit()

        runServer()

        stopKoin()
    }

    private fun runServer() {
        val config = get<IConfig>()
        embeddedServer(
            factory = CIO,
            host = config.host,
            port = config.port.toInt(),
        ) {
            get<IEndpoints>().endpoints.onEach { it.route(this) }
        }.start(true)
    }
}
