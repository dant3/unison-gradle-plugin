package com.unison.gradle

import com.unison.gradle.GradleConversions.ProjectExtParams
import org.gradle.api.{Action, Project, Task}

import scala.language.{dynamics, implicitConversions}

trait GradleConversions {
  implicit def action[T](function: T ⇒ Any):Action[T] = new Action[T] {
    override def execute(t: T): Unit = function.apply(t)
  }

  implicit class RichProject(project:Project) {
    lazy val ext:ProjectExtParams = new ProjectExtParams(project)
    // shortcut methods
    def findTask(taskName:String) = Option(project.getTasks.findByName(taskName))
    def applyPlugin(pluginId:String) = project.getPlugins.apply(pluginId)
    def createTask(taskName:String) = project.getTasks.create(taskName)
    def createTask(taskName:String, taskConfig: Task ⇒ Any) = project.getTasks.create(taskName, taskConfig)
    def configureExtension[T](config:T ⇒ Any)(implicit manifest: Manifest[T]) = {
      config.apply(project.getExtensions.findByType(manifest.runtimeClass).asInstanceOf[T])
    }
    def toGradle = project
  }
}

object GradleConversions extends GradleConversions {
  class ProjectExtParams(project:RichProject) extends Dynamic {
    def selectDynamic(name:String):Option[AnyRef] = {
      val extProps = project.toGradle.getConvention.getExtraProperties
      if (extProps.has(name)) {
        Some(extProps.get(name))
      } else {
        None
      }
    }
    def updateDynamic(name: String)(value: Any) = {
      project.toGradle.getConvention.getExtraProperties.set(name, value)
    }
    def getOption(name:String) = selectDynamic(name)
    def get[T](name:String)(implicit manifest:Manifest[T]):Option[T] = for {
      value ← getOption(name)
      if manifest.runtimeClass.isAssignableFrom(value.getClass)
    } yield value.asInstanceOf[T]
    def set(name:String)(value:Any) = updateDynamic(name)(value)
    def has(name:String) = project.toGradle.getConvention.getExtraProperties.has(name)
  }
}