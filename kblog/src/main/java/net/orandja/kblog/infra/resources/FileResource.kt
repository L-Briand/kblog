package net.orandja.kblog.infra.resources

import net.orandja.kblog._domain.resources.IResource
import java.io.File
import java.io.InputStream

class FileResource(private val file: File) : IResource {
    override fun name(): String? = file.name
    override fun content(): InputStream = file.inputStream()
}