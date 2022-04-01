package net.orandja.kblog.infra.resources

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import net.orandja.kblog._domain.IConfig
import net.orandja.kblog._domain.resources.IResource
import net.orandja.kblog._domain.resources.IResourceManager
import net.orandja.kblog.cases.toPath
import java.io.IOException
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

class ResourceManager(private val config: IConfig) : IResourceManager {
    private fun resource(sourceDir: String, name: String): IResource? {
        val resourceFilePath = Path(sourceDir.toPath().toString(), name)
        if (!resourceFilePath.exists() || !resourceFilePath.isRegularFile()) return null

        return try {
            FileResource(resourceFilePath.toFile())
        } catch (e: IOException) {
            null
        }
    }

    override fun publicResource(name: String): IResource? = resource(config.public, name)
    override fun templateResource(name: String): IResource? = resource(config.templates, name)

    override fun allPublic(ext: String): Flow<IResource> = flow {
        config.public.toPath().toFile().walk().onEach {
            if (it.isFile) runBlocking { emit(FileResource(it)) }
        }
    }

    override fun allTemplates(ext: String): Flow<IResource> = flow {
        config.templates.toPath().toFile().walk().onEach {
            if (it.isFile) runBlocking { emit(FileResource(it)) }
        }
    }

    // TODO : Implement caching system
    override fun cache(res: IResource): IResource = res
    override fun clear(res: IResource) = Unit
}