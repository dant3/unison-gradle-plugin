package com.unison.gradle.plugin

import com.unison.api.UnisonID
import com.unison.gradle.GradleConversions
import com.unison.gradle.plugin.Context.configuredParameter
import org.codehaus.groovy.control.ConfigurationException
import org.gradle.api.{Nullable, Project}

case class Context(project:Project, ext:UnisonGradlePlugin.Configuration) extends GradleConversions {

  def roomID = UnisonID.fromString(
    configuredParameter(project.ext.roomID.map(_.toString).orElse(Option(ext.roomID)), noRoomIDError)
  )
  def topicID = UnisonID.fromString(
    configuredParameter(project.ext.topicID.map(_.toString).orElse(Option(ext.topicID)), noTopicIDError)
  )
  def commentText = configuredParameter(ext.commentText, noCommentText).toString

  def createClient = ext.clientFactory.tupled.apply(credentials)
  def credentials:(String, String) = {
    val credentials = for {
      login ← Option(ext.login)
      password ← Option(ext.password)
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
