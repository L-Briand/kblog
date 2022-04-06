package net.orandja.kblog.cases

import net.orandja.kblog._domain.IConfig
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.system.exitProcess

class PathValidation(private val config: IConfig) {
    fun validateConfigPathsOrExit() {
        listOf(Path(config.resources), Path(config.public), Path(config.templates)).forEach {
            if (!it.exists()) {
                System.err.println("Directory not found (${it.absolutePathString()})")
                exitProcess(1)
            }
            if (!it.isDirectory()) {
                System.err.println("${it.absolutePathString()} should be a directory")
                exitProcess(1)
            }
        }
    }
}
