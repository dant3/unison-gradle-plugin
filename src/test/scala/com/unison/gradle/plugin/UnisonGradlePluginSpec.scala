package com.unison.gradle.plugin

import java.util.Collections

import com.unison.api.UnisonApi.NewCommentData
import com.unison.api._
import com.unison.gradle.GradleConversions._
import org.gradle.api.{Action, Project, Task}
import org.gradle.testfixtures.ProjectBuilder
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.EasyMockSugar
import org.scalatest.{BeforeAndAfterEach, Matchers, WordSpec}

import scala.util.Random

@RunWith(classOf[JUnitRunner])
class UnisonGradlePluginSpec extends WordSpec with Matchers with EasyMockSugar with BeforeAndAfterEach {
  var project:Project = _

  override def beforeEach() = {
    project = ProjectBuilder.builder.build()
    project.applyPlugin("unison-plugin")
  }


  "UnisonGradlePlugin" should {
    "add a task to list rooms" in {
      project.findTask(tasks.listRooms) shouldBe defined
    }
    "add a task to list topics" in {
      project.findTask(tasks.listTopics) shouldBe defined
    }
    "add a task to create a comment" in {
      project.findTask(tasks.createComment) shouldBe defined
    }

    "use UnisonApi to list rooms" in {
      val unisonApiMock = mock[UnisonApi]

      project.configureExtension[UnisonGradlePlugin.Configuration] { implicit ext ⇒ import ext._
        login = randomString
        password = randomString
        clientFactory = verifyLoginPassword((login, password)).andThenReturn(unisonApiMock).untupled
      }

      expecting {
        unisonApiMock.getRooms.andReturn(Collections.emptyList[Room])
      }; whenExecuting(unisonApiMock) {
        project.findTask(tasks.listRooms).get.execute()
      }
    }

    "use UnisonApi to list topics" in {
      val unisonApiMock = mock[UnisonApi]
      val roomID = randomUnisonID

      project.ext.roomID = roomID
      project.configureExtension[UnisonGradlePlugin.Configuration] { implicit ext ⇒ import ext._
        login = randomString
        password = randomString
        clientFactory = verifyLoginPassword((login, password)).andThenReturn(unisonApiMock).untupled
      }

      expecting {
        unisonApiMock.getTopics(UnisonID.fromString(roomID)).andReturn(Collections.emptyList[Topic])
      }; whenExecuting(unisonApiMock) {
        project.findTask(tasks.listTopics).get.execute()
      }
    }

    "use UnisonApi to post comment" in {
      val unisonApiMock = mock[UnisonApi]
      val roomID = randomUnisonID
      val topicID = randomUnisonID
      val commentText = randomString

      project.ext.topicID = topicID
      project.configureExtension[UnisonGradlePlugin.Configuration] { implicit ext ⇒
        ext.login = randomString
        ext.password = randomString
        ext.roomID = roomID
        ext.commentText = commentText
        ext.clientFactory = verifyLoginPassword((ext.login, ext.password)).andThenReturn(unisonApiMock).untupled
      }

      expecting {
        unisonApiMock.createComment(UnisonID.fromString(roomID),
                                    UnisonID.fromString(topicID),
                                    NewCommentData.create(commentText)).andReturn(mock[Comment])
      }; whenExecuting(unisonApiMock) {
        project.findTask(tasks.createComment).get.execute()
      }
    }
  }


  def randomUnisonID = Random.alphanumeric.take(11).mkString
  def randomString = Random.alphanumeric.take(42).mkString
  def verifyLoginPassword(expected:(String, String)) = { (actual:(String, String)) ⇒ actual shouldBe expected }


  implicit class RichTask(task:Task) {
    // This implementation is very rough and does not involves dependency resolution.
    // To test tasks with dependencies you one needs to create integration testing.
    // Unfortunately as of may 2015 Gradle still does not have integration testing solution.
    def execute():Unit = task.getActions.toArray.
        map(_.asInstanceOf[Action[Any]]).foreach(_.execute(null))
  }
  
  implicit class TupledFunction1[I1,I2,O](fn:((I1, I2)) ⇒ O) {
    def untupled: (I1,I2) ⇒ O = (i1:I1, i2:I2) ⇒ fn((i1, i2))
    def andThenReturn[O2](block: ⇒ O2) = fn.andThen((_) ⇒ block)
  }
}
