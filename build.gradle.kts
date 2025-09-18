plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.forret"
version = file("VERSION.md").readText().trim()

defaultTasks("build")

repositories {
    mavenCentral()
}

intellij {
    // Target PhpStorm 2025.2.1 so we compile and verify against the same build family (252.*).
    version.set("2025.2.1")
    type.set("PS")
    // Only require the built-in Terminal plugin; avoid Java plugin so PhpStorm is supported.
    plugins.set(listOf("org.jetbrains.plugins.terminal"))
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    // Use IntelliJ Platform's Kotlin stdlib; don't bundle our own
    compileOnly(kotlin("stdlib"))
}

tasks {
    patchPluginXml {
        // PhpStorm 2025.2.1 corresponds to build family 252.*
        sinceBuild.set("252")
        untilBuild.set("252.*")
        pluginDescription.set(
            """
            LLM Brains: open popular CLI coding agents (Claude, Codex, Gemini) in an IDE terminal.
            Adds a toolbar button (🫴) with options and a check to see what is installed.
            """.trimIndent()
        )
        changeNotes.set("Initial version.")
    }

    // Ensure `./gradlew build` also produces the plugin ZIP
    named("build") {
        dependsOn("buildPlugin")
    }

    named("buildSearchableOptions") {
        enabled = false
    }

    runIde {
        // Optionally point to a specific IDE install
        // ideDir.set(file("/Applications/PhpStorm.app/Contents"))
    }
}
