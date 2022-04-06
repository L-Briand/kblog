package net.orandja.kblog._domain

import java.io.File

interface IConfig {
    val port: Short
    val listen: String
    val resources: String
    val public: String get() = "$resources${File.separator}public"
    val templates: String get() = "$resources${File.separator}templates"
}
