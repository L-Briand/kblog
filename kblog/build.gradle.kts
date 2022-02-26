plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    application
}

group = rootProject.property("group") as String
version = rootProject.property("libVersion") as String

application {
    mainClass.set("${project.group}.MainKt")
}

dependencies {
    // cli parameters
    implementation("com.xenomachina:kotlin-argparser:2.0.7")

    // web service
    val ktorVersion = "2.0.0-beta-1"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-compression:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.10")

    // markdown parser - html generator
    val commonmarkVersion = "0.18.1"
    implementation("org.commonmark:commonmark:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-ins:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-gfm-tables:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-autolink:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-gfm-strikethrough:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-image-attributes:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-task-list-items:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-heading-anchor:$commonmarkVersion")
    implementation("org.commonmark:commonmark-ext-yaml-front-matter:$commonmarkVersion")

    // Template processor
    implementation("com.soywiz.korlibs.korte:korte:2.4.12")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// KtLint configuration
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}
