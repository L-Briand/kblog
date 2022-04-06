package net.orandja.kblog.infra.endpoints

import net.orandja.kblog._domain.IKtorModule
import net.orandja.kblog._domain.endpoints.IEndpoints
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class Endpoints : IEndpoints, KoinComponent {
    override val endpoints: List<IKtorModule> = listOf(
        MarkdownDocument(get(), get()),
        ResourcesProvider(get())
    )
}
