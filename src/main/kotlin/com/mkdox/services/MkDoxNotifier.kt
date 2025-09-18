package com.mkdox.services

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

object MkDoxNotifier {
    private const val GROUP_ID = "MkDox"

    fun info(project: Project, content: String) = notify(project, content, NotificationType.INFORMATION)

    fun warn(project: Project, content: String) = notify(project, content, NotificationType.WARNING)

    fun error(project: Project, content: String) = notify(project, content, NotificationType.ERROR)

    private fun notify(project: Project, content: String, type: NotificationType) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup(GROUP_ID)
            .createNotification(content, type)
            .notify(project)
    }
}
