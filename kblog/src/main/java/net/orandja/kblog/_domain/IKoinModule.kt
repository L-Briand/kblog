package net.orandja.kblog._domain

import org.koin.core.module.Module

interface IKoinModule {
    val modules: List<Module>
}
