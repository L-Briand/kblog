package net.orandja.kblog.cases

import net.orandja.kblog._domain.IKoinModule
import org.koin.core.module.Module
import org.koin.dsl.module

object KoinModuleCases : IKoinModule {
    private val default = module {
        factory { PathValidation(get()) }
    }
    override val modules: List<Module> = listOf(default)
}