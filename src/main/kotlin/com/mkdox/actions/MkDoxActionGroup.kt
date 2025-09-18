package com.mkdox.actions

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.IconLoader
import com.mkdox.services.MkDoxStateService

class MkDoxActionGroup : ActionGroup(), DumbAware {
    private val createAction = MkDoxCreateAction()
    private val serveAction = MkDoxServeAction()

    private val activeIcon = IconLoader.getIcon("/icons/mkdoxActive.png", javaClass)
    private val inactiveIcon = IconLoader.getDisabledIcon(activeIcon)

    override fun getChildren(event: AnActionEvent?): Array<AnAction> {
        val project = event?.project ?: return AnAction.EMPTY_ARRAY
        val state = MkDoxStateService.projectState(project)
        return if (state.isReady) {
            arrayOf<AnAction>(serveAction)
        } else {
            arrayOf<AnAction>(createAction)
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val presentation = event.presentation
        if (project == null) {
            presentation.isEnabledAndVisible = false
            return
        }

        presentation.isEnabledAndVisible = true
        val state = MkDoxStateService.projectState(project)
        presentation.icon = if (state.isReady) activeIcon else inactiveIcon
        presentation.text = if (state.isReady) "Serve mkdox" else "Create mkdox"
        presentation.description = state.describe()
    }

    private fun MkDoxStateService.State.describe(): String =
        if (isReady) {
            "Serve mkdox documentation"
        } else {
            when {
                !hasMkdocsFile -> "Missing mkdocs.yml in project root"
                !hasDocsDirectory -> "Missing docs/ directory"
                !hasScript -> "Missing mkdox.sh script"
                else -> "mkdox prerequisites incomplete"
            }
        }
}
