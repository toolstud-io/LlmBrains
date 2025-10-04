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
    // Build against IntelliJ IDEA 2023.1 (231.*) so the plugin stays compatible with all IDEs released in the last two years.
    version.set("2023.1")
    type.set("IC")
    // Only require the built-in Terminal plugin so every JetBrains IDE with a terminal can load us.
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
        // 231 corresponds to the 2023.1 release family; newer IDEs remain compatible without an explicit upper bound.
        sinceBuild.set("251")
        // The latest version of any IDE in the 2023.1 release family is 231.*; we want to stay compatible with all IDEs released in the last two years.
        untilBuild.set("252.*")
        pluginDescription.set(
            """
            LLM Brains: open popular CLI coding agents (Claude, Codex, Gemini ...) in an IDE terminal.
            Adds a toolbar button (🫴) with options and a check to see what is installed.
            """.trimIndent()
        )
        changeNotes.set("Added Grok, Droid, Warp CLI agents")
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
