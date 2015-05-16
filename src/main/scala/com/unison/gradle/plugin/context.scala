package com.unison.gradle.plugin

import com.unison.api.UnisonID
import com.unison.gradle.GradleConversions
import com.unison.gradle.plugin.Context.configuredParameter
import org.codehaus.groovy.control.ConfigurationException
import org.gradle.api.{Nullable, Project}

case class Context(project:Project, config:UnisonGradlePlugin.Extension) extends GradleConversions {

  def roomID = UnisonID.fromString(
    configuredParameter(project.ext.roomID.orElse(Option(config.roomID)), noRoomIDError).toString
  )
  def topicID = UnisonID.fromString(
    configuredParameter(project.ext.topicID.orElse(Option(config.topicID)), noTopicIDError).toString
  )
  def commentText = configuredParameter(config.commentText, noCommentText).toString

  def createClient = config.clientFactory.tupled.apply(credentials)
  def credentials:(String, String) = {
    val credentials = for {
      login ← Option(config.login)
      password ← Option(config.password)
    } yield (login, password)
    configuredParameter(credentials, noLoginPasswordError)
  }
}

object Context {
  def configuredParameter[T](@Nullable value:T, errorMessage:String):T = configuredParameter(Option(value), errorMessage)
  def configuredParameter[T](value:Option[T], errorMessage:String):T = value match {
    case Some(paramValue) ⇒ paramValue
    case None ⇒ throw new ConfigurationException(errorMessage)
  }
}
