package org.github.dant3

import org.github.dant3.GroovyConversions._
import org.gradle.api.{Plugin, Project, Task}

class UnisonGradlePlugin extends Plugin[Project] {
    val extension = new UnisonGradleExtension

    def apply(project:Project):Unit = {
        project.getExtensions.add("unison", extension)
        project.getTasks.create("listRooms").configure { task:Task ⇒
            task.setGroup("unison")
            task.setDescription("Prints list of Unison rooms that is available")
            task.doFirst {
                val (login, password) = extension.getCredentials
                tasks.listRooms(login, password)
            }
        }
    }
}
