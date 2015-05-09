package org.github.dant3

import com.unison.api.UnisonApi.NewCommentData
import com.unison.api.UnisonID
import com.unison.api.client.UnisonApiClient
import org.joda.time.ReadableInstant

import scala.collection.JavaConversions._

object tasks {
  implicit def comparableOrdering[T <: Comparable[T]]:Ordering[T] = Ordering.ordered[T](identity)
  implicit val dateTimeOrdering:Ordering[ReadableInstant] = comparableOrdering[ReadableInstant].reverse

  def listRooms(login:String, password:String) = {
    val roomsListString =
      UnisonApiClient.create(login, password).
          getRooms.
          sortBy(_.getLastUpdated.asInstanceOf[ReadableInstant]).
          map(room ⇒ s"""${room.getID} > "${room.getTitle}""").
          mkString("\n")
    println(roomsListString)
  }

  def listTopics(login:String, password:String, roomID:UnisonID) = {
    val topicsListString =
      UnisonApiClient.create(login, password).
          getTopics(roomID).
          sortBy(_.getLastCommented.asInstanceOf[ReadableInstant]).
          map(topic ⇒ s"""${topic.getID} > "${topic.getSubject}" by ${topic.getAuthor.getName}""").
          mkString("\n")
    println(topicsListString)
  }

  def createComment(login:String, password:String, roomID:UnisonID, topicID:UnisonID, commentText:String) = {
    UnisonApiClient.create(login, password).
        createComment(roomID, topicID, NewCommentData.create(commentText))
  }
}
