package com.mkdox.services

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class MkDoxStateServiceTest {
    @TempDir
    lateinit var tempDir: Path

    @AfterEach
    fun tearDown() {
        MkDoxStateService.pathOverrideForWhich = null
    }

    @Test
    fun shouldDetectReadyStateWhenAllArtifactsExist() {
        val docsDir = tempDir.resolve("docs")
        docsDir.createDirectories()
        docsDir.resolve("blog").createDirectories()
        tempDir.resolve("mkdocs.yml").writeText("site_name: test")
        val scriptsDir = tempDir.resolve("bin")
        scriptsDir.createDirectories()
        val script = scriptsDir.resolve("mkdox.sh")
        script.writeText("#!/bin/sh\nexit 0\n")
        script.toFile().setExecutable(true)
        MkDoxStateService.pathOverrideForWhich = scriptsDir.toString()

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
