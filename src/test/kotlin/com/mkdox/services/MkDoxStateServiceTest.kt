package com.mkdox.services

import com.mkdox.services.MkDoxStateService
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class MkDoxStateServiceTest {
    @TempDir
    lateinit var tempDir: Path

    @Test
    fun shouldDetectReadyStateWhenAllArtifactsExist() {
        val docsDir = tempDir.resolve("docs")
        docsDir.createDirectories()
        docsDir.resolve("blog").createDirectories()
        tempDir.resolve("mkdocs.yml").writeText("site_name: test")
        val script = tempDir.resolve("mkdox.sh")
        Files.writeString(script, "#!/bin/sh\nexit 0\n")
        script.toFile().setExecutable(true)

        val state = MkDoxStateService.stateForPath(tempDir)

        assertTrue(state.hasDocsDirectory)
        assertTrue(state.hasMkdocsFile)
        assertTrue(state.hasBlogDirectory)
        assertTrue(state.hasScript)
        assertTrue(state.isReady)
    }

    @Test
    fun shouldDetectMissingArtifacts() {
        val state = MkDoxStateService.stateForPath(tempDir)

        assertFalse(state.hasDocsDirectory)
        assertFalse(state.hasMkdocsFile)
        assertFalse(state.hasBlogDirectory)
        assertFalse(state.hasScript)
        assertFalse(state.isReady)
    }
}
