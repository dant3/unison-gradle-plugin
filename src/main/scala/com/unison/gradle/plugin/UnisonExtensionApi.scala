package com.unison.gradle.plugin

import com.unison.api.UnisonID
import org.gradle.api.Project

import scala.language.implicitConversions

class UnisonExtensionApi(project:Project) { self:UnisonGradlePlugin.Extension ⇒
  private val context = Context(project, self)

  def createComment(roomID:CharSequence, topicID:CharSequence, commentText:CharSequence) = {
    actions.createComment(context.createClient, roomID, topicID, commentText.toString)
  }

  def createComment(commentText:CharSequence) = {
    actions.createComment(context.createClient, context.roomID, context.topicID, commentText.toString)
  }

  def createComment() = {
    actions.createComment(context.createClient, context.roomID, context.topicID, context.commentText)
  }

  implicit private def toUnisonID(id:CharSequence):UnisonID = UnisonID.fromString(notNull(id).toString)

  def notNull[T <: AnyRef](ref:T):T = {
    require(ref != null, "null value is not allowed")
    ref
  }
}
