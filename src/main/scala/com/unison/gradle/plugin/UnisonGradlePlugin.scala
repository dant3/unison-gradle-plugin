package com.unison.gradle.plugin

import com.unison.api.UnisonApi
import com.unison.api.client.UnisonApiClient
import com.unison.gradle.GroovyConversions
import com.unison.gradle.plugin.UnisonGradlePlugin.Extension
import groovy.lang.Closure
import org.codehaus.groovy.control.ConfigurationException
import org.gradle.api.{Nullable, Plugin, Project}

import scala.beans.BeanProperty

class UnisonGradlePlugin extends Plugin[Project] {
  private var _extension:Option[Extension] = None
  def extension = _extension match {
    case Some(ext) ⇒ ext
    case None ⇒ throw new RuntimeException("Extension is not created yet")
  }

  def apply(project:Project):Unit = {
    val extension = new Extension(project)
    _extension = Some(extension)
    project.getExtensions.add("unison", extension)
    tasks.create(project, this)
  }
}


object UnisonGradlePlugin {
  class Extension(project:Project) extends UnisonExtensionApi(project) {
    @Nullable @BeanProperty var login:String = _
    @Nullable @BeanProperty var password:String = _

    @Nullable @BeanProperty var roomID:String = _
    @Nullable @BeanProperty var topicID:String = _
    private var commentTextSupplier: Option[() ⇒ CharSequence] = None

    // scala api
    def commentText:Option[CharSequence] = commentTextSupplier.map(_.apply())
    def commentText_=(value:CharSequence):Unit = commentTextSupplier = Some(() ⇒ value)
    def commentText_=(supplier:() ⇒ CharSequence):Unit = commentTextSupplier = Some(supplier)
    // groovy api
    def commentText(value:CharSequence):Unit = setCommentText(value)
    def commentText(closure:Closure[CharSequence]):Unit = setCommentText(closure)
    def getCommentText:CharSequence = commentText.orNull
    def setCommentText(value:CharSequence):Unit = { commentText = value }
    def setCommentText(closure:Closure[CharSequence]):Unit = {
      commentText = GroovyConversions.toScalaFunction(closure)
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
