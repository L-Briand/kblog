package net.orandja.kblog._domain

import io.ktor.server.application.*
import io.ktor.server.routing.*

/** Just an interface to help organize code */
interface IKtorModule {
    fun route(app: Application) = app.routing { route() }
    fun Routing.route()
}
