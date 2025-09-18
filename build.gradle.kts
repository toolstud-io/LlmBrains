plugins {
    kotlin("jvm") version "1.9.25"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.example"
version = file("VERSION.md").readText().trim()

defaultTasks("build")

repositories {
    mavenCentral()
}

intellij {
    // Use a modern IntelliJ Platform build so the Terminal API is available.
    version.set("2024.1")
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
        sinceBuild.set("241")
        // Allow running on 2025.2 (build 252.*) and later minor updates
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

    runIde {
        // Optionally point to a specific IDE install
        // ideDir.set(file("/Applications/PhpStorm.app/Contents"))
    }
}
