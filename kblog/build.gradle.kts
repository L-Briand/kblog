
plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.20"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    application
}

group = rootProject.property("group") as String
version = rootProject.property("libVersion") as String
val main = "${project.group}.Main"

application {
    mainClass.set(main)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-opt-in=org.mylibrary.OptInAnnotation"
    }
}

tasks.withType<JavaCompile>().all {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

dependencies {
    // cli parameters
    implementation("com.xenomachina:kotlin-argparser:2.0.7")
    implementation("commons-daemon:commons-daemon:1.3.0")

    // Serialization
    implementation("com.charleskorn.kaml:kaml:0.43.0")

    // web service
    val ktorVersion = "2.0.0"
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-cors:$ktorVersion")
    implementation("io.ktor:ktor-server-compression:$ktorVersion")
    implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    // DI
    val koinVersion = "3.2.0-beta-1"
    implementation("io.insert-koin:koin-core:$koinVersion")

    // markdown parser - html generator
    val commonmarkVersion = "0.18.2"
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
    implementation("com.soywiz.korlibs.korte:korte:2.7.0")
    implementation("net.orandja.tt:TT:0.0.4")

    // tests
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

// KtLint configuration
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    disabledRules.set(setOf("no-wildcard-imports"))
}

tasks.register("fatJar", Jar::class.java) {
    archiveClassifier.set("fat")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes("Main-Class" to main) }
    // Internal classes
    from(sourceSets.main.get().output)
    // External Dependencies
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    dependsOn("build")
}
