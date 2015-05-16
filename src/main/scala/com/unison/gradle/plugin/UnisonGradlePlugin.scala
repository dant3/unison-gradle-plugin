package com.unison.gradle.plugin

import com.unison.api.UnisonApi
import com.unison.api.client.UnisonApiClient
import com.unison.gradle.GroovyConversions
import com.unison.gradle.plugin.UnisonGradlePlugin.Configuration
import groovy.lang.Closure
import org.codehaus.groovy.control.ConfigurationException
import org.gradle.api.{Nullable, Plugin, Project}

import scala.beans.BeanProperty

class UnisonGradlePlugin extends Plugin[Project] {
  val extension = new Configuration

  def apply(project:Project):Unit = {
    project.getExtensions.add("unison", extension)
    tasks.create(project, this)
  }
}


object UnisonGradlePlugin extends GroovyConversions {
  class Configuration {
    @Nullable @BeanProperty var login:String = _
    @Nullable @BeanProperty var password:String = _

    @Nullable @BeanProperty var roomID:String = _
    @Nullable @BeanProperty var topicID:String = _
    private var commentTextSupplier: Option[() ⇒ String] = None

    // scala api
    def commentText:Option[String] = commentTextSupplier.map(_.apply())
    def commentText_=(value:String):Unit = commentTextSupplier = Some(() ⇒ value)
    def commentText_=(supplier:() ⇒ String):Unit = commentTextSupplier = Some(supplier)
    // groovy api
    def commentText(value:String):Unit = setCommentText(value)
    def commentText(closure:Closure[String]):Unit = setCommentText(closure)
    def getCommentText:String = commentText.orNull
    def setCommentText(value:String):Unit = { commentText = value }
    def setCommentText(closure:Closure[String]):Unit = {
      commentText = closure
    }

    def login(login:String):Unit = this.login = login
    def password(password:String):Unit = this.password = password

    private[plugin] var clientFactory:(String, String) ⇒ UnisonApi = UnisonApiClient.create
    private[plugin] def getCredentials:(String, String) = if (login == null || password == null) {
      throw new ConfigurationException(noLoginPasswordError)
    } else {
      (login, password)
    }
  }
}
