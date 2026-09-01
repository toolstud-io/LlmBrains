import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.commonmark:commonmark:0.22.0")
    }
}

plugins {
    kotlin("jvm") version "2.1.0"
    id("org.jetbrains.intellij") version "1.17.3"
}

fun markdownToHtml(markdown: String): String {
    val parser = Parser.builder().build()
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(parser.parse(markdown))
}

group = "com.forret"
version = file("VERSION.md").readText().trim()

defaultTasks("build")

repositories {
    mavenCentral()
}

intellij {
    // Build against IntelliJ IDEA 2025.1 (251.*) to match our sinceBuild and use the new Terminal services API.
    version.set("2025.1")
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
    testImplementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    patchPluginXml {
        // 251 corresponds to the 2025.1 release family; newer IDEs remain compatible without an explicit upper bound.
        sinceBuild.set("251")
        // Support up to 2026.2 release family (262.*) for PhpStorm 2026.2 and other recent IDEs.
        untilBuild.set("262.*")
        pluginDescription.set(
            """
            LLM Brains: open popular CLI coding agents (Claude, Codex, Gemini ...) in an IDE terminal.
            Adds a toolbar button (🫴) with options and a check to see what is installed.
            """.trimIndent()
        )
        changeNotes.set(markdownToHtml(file("CHANGES.md").readText()))
    }

    // Ensure `./gradlew build` also produces the plugin ZIP
    named("build") {
        dependsOn("buildPlugin")
    }

    named("buildSearchableOptions") {
        enabled = false
    }

    test {
        useJUnitPlatform()
        // gradle-intellij-plugin 1.x crashes (IndexOutOfBoundsException in resolveIdeHomeVariable) while parsing the
        // JVM args of IntelliJ 2025.1's product-info.json. Our tests are pure Kotlin and don't need the IDE runtime.
        doFirst {
            jvmArgumentProviders.clear()
            systemProperties = systemProperties.filterKeys { !it.startsWith("idea.") && it != "java.system.class.loader" }
            // The IDE's testFramework jar registers a JUnit 5 session listener that needs JUnit 4 and a running platform.
            classpath = classpath.filter { !it.name.startsWith("testFramework") }
        }
    }

    runIde {
        // Optionally point to a specific IDE install
        // ideDir.set(file("/Applications/PhpStorm.app/Contents"))
    }
}
