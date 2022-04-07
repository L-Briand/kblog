package net.orandja.kblog

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import net.orandja.kblog._domain.IConfig
import net.orandja.kblog._domain.endpoints.IEndpoints
import net.orandja.kblog.cases.KoinModuleCases
import net.orandja.kblog.cases.PathValidation
import net.orandja.kblog.infra.Config
import net.orandja.kblog.infra.KoinModuleInfra
import org.apache.commons.daemon.Daemon
import org.apache.commons.daemon.DaemonContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class Main : KoinComponent, Daemon {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().apply {
                init(args)
                waitAtStart = true
                start()
                stop()
                destroy()
            }
        }
    }

    private var waitAtStart = false
    private lateinit var server: CIOApplicationEngine

    override fun init(context: DaemonContext) = init(context.arguments)
    fun init(args: Array<String>) {
        println("--- INIT ---")
        val config = mainBody { ArgParser(args).parseInto(::Config) }
        val configMod = module { single<IConfig> { config } }
        startKoin {
            modules(configMod)
            modules(KoinModuleCases.modules)
            modules(KoinModuleInfra.modules)
        }
        get<PathValidation>().validateConfigPathsOrExit()
    }

    override fun start() {
        println("--- START ---")
        val config = get<IConfig>()
        server = embeddedServer(
            factory = CIO,
            host = config.listen,
            port = config.port.toInt(),
        ) {
            get<IEndpoints>().endpoints.onEach { it.route(this) }
        }
        server.start(waitAtStart)
    }

    override fun stop() {
        println("--- STOP ---")
        server.stop(1000L, 10000L)
    }

    override fun destroy() {
        println("--- DESTROY ---")
        stopKoin()
    }
}
