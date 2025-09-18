import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.intellij") version "1.17.3"
}

group = "com.mkdox"
version = file("VERSION.md").readText().trim()

repositories {
    mavenCentral()
}

intellij {
    version.set("2023.3")
    plugins.set(listOf("org.jetbrains.plugins.terminal"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
        }
    }

    patchPluginXml {
        sinceBuild.set("233")
        untilBuild.set("")
    }

    test {
        useJUnitPlatform()
    }

    runPluginVerifier {
        ideVersions.set(listOf("IC-2023.3"))
    }
}

dependencies {
    compileOnly(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
