package net.orandja.kblog.infra

import net.orandja.kblog._domain.IKoinModule
import net.orandja.kblog._domain.endpoints.IEndpoints
import net.orandja.kblog._domain.markdown.IMarkdownRenderer
import net.orandja.kblog._domain.resources.IResourceManager
import net.orandja.kblog.infra.endpoints.Endpoints
import net.orandja.kblog.infra.markdown.MarkdownRenderer
import net.orandja.kblog.infra.resources.ResourceManager
import org.koin.core.module.Module
import org.koin.dsl.module

object KoinModuleInfra : IKoinModule {
    private val endpoints = module {
        single<IEndpoints> { Endpoints() }
        single<IResourceManager> { ResourceManager(get()) }
        single<IMarkdownRenderer> { MarkdownRenderer() }
    }
    override val modules: List<Module> = listOf(endpoints)
}