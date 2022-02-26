package net.orandja.kblog

import io.ktor.server.application.*
import io.ktor.server.routing.*

/** Just an interface to help organize code */
interface KtorModule {
    fun module(app: Application) = app.routing { module() }
    fun Routing.module()
}
