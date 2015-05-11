package com.unison.gradle

import org.gradle.api.{Action, Project, Task}

import scala.language.implicitConversions

trait GradleConversions {
  implicit def action[T](function: T ⇒ Any):Action[T] = new Action[T] {
    override def execute(t: T): Unit = function.apply(t)
  }

  implicit class RichProject(project:Project) {
    def findTask(taskName:String) = Option(project.getTasks.findByName(taskName))
    def applyPlugin(pluginId:String) = project.getPlugins.apply(pluginId)
    def createTask(taskName:String) = project.getTasks.create(taskName)
    def createTask(taskName:String, taskConfig: Task ⇒ Any) = project.getTasks.create(taskName, taskConfig)
    def configureExtension[T](config:T ⇒ Any)(implicit manifest: Manifest[T]) = {
      config.apply(project.getExtensions.findByType(manifest.runtimeClass).asInstanceOf[T])
    }
  }
}

object GradleConversions extends GradleConversions