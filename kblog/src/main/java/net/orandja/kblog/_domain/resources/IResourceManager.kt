package net.orandja.kblog._domain.resources

import kotlinx.coroutines.flow.Flow

interface IResourceManager {
    fun publicResource(name: String): IResource?
    fun templateResource(name: String): IResource?
    fun allPublic(ext: String): Flow<IResource>
    fun allTemplates(ext: String): Flow<IResource>
    fun cache(res: IResource): IResource
    fun clear(res: IResource)
}