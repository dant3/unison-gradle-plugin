package org.github.dant3

import org.codehaus.groovy.control.ConfigurationException
import org.gradle.api.{Project, Action, Task}
import org.gradle.testfixtures.ProjectBuilder
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}


@RunWith(classOf[JUnitRunner])
class UnisonGradlePluginSpec extends WordSpec with Matchers with MockFactory {
  "UnisonGradlePlugin" should {
    "adds a task which lists rooms" in {
      val project = ProjectBuilder.builder.build()
      project.applyPlugin("org.github.dant3.unison-plugin")
      an[ConfigurationException] shouldBe thrownBy {
        project.findTask("listRooms").execute()
      }
      project.configureExtension[UnisonGradleExtension] { ext:UnisonGradleExtension ⇒
        ext.login = "foo"
        ext.password = "bar"
      }
    }
  }

  implicit class RichTask(task:Task) {
    def execute():Unit = task.getActions.toArray.
        map(_.asInstanceOf[Action[Any]]).foreach(_.execute(null))
  }

  implicit class RichProject(project:Project) {
    import GradleConversions._

    def findTask(taskName:String) = project.getTasks.findByName(taskName)
    def applyPlugin(pluginId:String) = project.getPlugins.apply(pluginId)
    def configureExtension[T](configuration: T ⇒ Any)(implicit manifest:Manifest[T]) = {
      project.getExtensions.configure(manifest.runtimeClass.asInstanceOf[Class[T]], configuration)
    }
  }
}
