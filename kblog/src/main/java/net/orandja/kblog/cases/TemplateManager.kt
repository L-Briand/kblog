package net.orandja.kblog.cases

import kotlinx.coroutines.flow.toList
import net.orandja.kblog._domain.resources.IResourceManager
import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer

class TemplateManager(private val resources: IResourceManager) {
    lateinit var groupTemplate: TemplateRenderer
    suspend fun init() {
        groupTemplate = TT.group(
            resources.allTemplates().toList().associate { it.name()!! to TT.template(it.contentAsString()) }
        )
    }
}