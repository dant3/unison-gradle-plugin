package com.unison.gradle.plugin

import com.unison.gradle.{GroovyConversions, GradleConversions}
import org.gradle.api.{Project, Task}


object tasks {
  val group = "unison"
  val listRooms = "listRooms"
  val listTopics = "listTopics"
  val createComment = "createComment"

  def create(project:Project, plugin:UnisonGradlePlugin) = {
    import GradleConversions._

    implicit val ctx = Context(plugin.extension)
    project.createTask(listRooms, listRoomsTaskConfiguration)
    project.createTask(listTopics, listTopicsTaskConfiguration)
    project.createTask(createComment, createCommentTaskConfiguration)
  }


  import GroovyConversions._

  def listRoomsTaskConfiguration(implicit ctx: Context) = { task:Task ⇒
    task.setGroup(group)
    task.setDescription("Prints list of Unison rooms that is available")
    task.doFirst {
      actions.listRooms(ctx.createClient)
    }
  }


  def listTopicsTaskConfiguration(implicit ctx: Context) = { task:Task ⇒
    task.setGroup(group)
    task.setDescription("Prints list of Unison topics that is available in room supplied by configured roomId")
    task.doFirst {
      val roomID = ctx.roomID
      actions.listTopics(ctx.createClient, roomID)
    }
  }

  def createCommentTaskConfiguration(implicit ctx: Context) = { task:Task ⇒
    task.setGroup(group)
    task.setDescription("Creates a comment in room and topic supplied by plugin configuration")
    task.doFirst {
      val roomID = ctx.roomID
      val topicID = ctx.topicID
      val commentText = ctx.commentText
      actions.createComment(ctx.createClient, roomID, topicID, commentText)
    }
  }
}
