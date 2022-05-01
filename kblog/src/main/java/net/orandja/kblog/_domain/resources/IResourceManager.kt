package net.orandja.kblog._domain.resources

import kotlinx.coroutines.flow.Flow

interface IResourceManager {
    fun publicResource(name: String): IResource?
    fun templateResource(name: String): IResource?
    fun allPublic(): Flow<IResource>
    fun allTemplates(): Flow<IResource>
    fun cache(res: IResource): IResource
    fun clear(res: IResource)
}
