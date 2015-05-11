package com.unison.gradle.plugin

import com.unison.api.UnisonApi.NewCommentData
import com.unison.api.{UnisonApi, UnisonID}
import org.joda.time.ReadableInstant

import scala.collection.JavaConversions._

object actions {
  implicit def comparableOrdering[T <: Comparable[T]]:Ordering[T] = Ordering.ordered[T](identity)
  implicit val dateTimeOrdering:Ordering[ReadableInstant] = comparableOrdering[ReadableInstant].reverse

  def listRooms(client: ⇒ UnisonApi) = {
    val roomsListString =
          client.
          getRooms.
          sortBy(_.getLastUpdated.asInstanceOf[ReadableInstant]).
          map(room ⇒ s"""${room.getID} |> "${room.getTitle}"""").
          mkString("\n")
    println(roomsListString)
  }

  def listTopics(client: ⇒ UnisonApi, roomID:UnisonID) = {
    val topicsListString =
          client.
          getTopics(roomID).
          sortBy(_.getLastCommented.asInstanceOf[ReadableInstant]).
          map(topic ⇒ s"""${topic.getID} |> "${topic.getSubject}" by ${topic.getAuthor.getName}""").
          mkString("\n")
    println(topicsListString)
  }

  def createComment(client: ⇒ UnisonApi, roomID:UnisonID, topicID:UnisonID, commentText:String) = {
        client.
        createComment(roomID, topicID, NewCommentData.create(commentText))
  }
}
