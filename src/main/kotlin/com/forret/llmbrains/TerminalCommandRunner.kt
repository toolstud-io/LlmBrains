package com.forret.llmbrains

import com.intellij.openapi.project.Project

object TerminalCommandRunner {
    fun run(project: Project, title: String, command: String) {
        val workingDir = project.basePath ?: ""

        // Try the newest Terminal service API (available in recent IDEs)
        try {
            val serviceClass = Class.forName("org.jetbrains.plugins.terminal.TerminalService")
            val getInstance = serviceClass.getMethod("getInstance", Project::class.java)
            val service = getInstance.invoke(null, project)
            val createWidget = serviceClass.getMethod("createLocalShellWidget", String::class.java, String::class.java)
            val widget = createWidget.invoke(service, workingDir, title)
            val exec = widget.javaClass.getMethod("executeCommand", String::class.java)
            exec.invoke(widget, command)
            return
        } catch (_: Throwable) {
            // fall through to older APIs
        }

        // Fallback to TerminalView (deprecated in newest IDEs but present in 2023.x/2024.x)
        try {
            val viewClass = Class.forName("org.jetbrains.plugins.terminal.TerminalView")
            val getInstance = viewClass.getMethod("getInstance", Project::class.java)
            val view = getInstance.invoke(null, project)
            val createWidget = viewClass.getMethod("createLocalShellWidget", String::class.java, String::class.java)
            val widget = createWidget.invoke(view, workingDir, title)
            val exec = widget.javaClass.getMethod("executeCommand", String::class.java)
            exec.invoke(widget, command)
            return
        } catch (_: Throwable) {
            // fall through to oldest API
        }

        // Final fallback to TerminalToolWindowManager (older API)
        try {
            val mgrClass = Class.forName("org.jetbrains.plugins.terminal.TerminalToolWindowManager")
            val getInstance = mgrClass.getMethod("getInstance", Project::class.java)
            val mgr = getInstance.invoke(null, project)
            val createWidget = mgrClass.getMethod("createLocalShellWidget", String::class.java, String::class.java)
            val widget = createWidget.invoke(mgr, workingDir, title)
            val exec = widget.javaClass.getMethod("executeCommand", String::class.java)
            exec.invoke(widget, command)
            return
        } catch (_: Throwable) {
            // give up silently; in production we might notify the user
        }
    }
}
