package com.unison.gradle.plugin

import com.unison.api.UnisonApi
import com.unison.api.client.UnisonApiClient
import com.unison.gradle.GroovyConversions
import com.unison.gradle.plugin.UnisonGradlePlugin.Extension
import groovy.lang.Closure
import org.gradle.api.{Nullable, Plugin, Project}

import scala.beans.BeanProperty

class UnisonGradlePlugin extends Plugin[Project] {
  def apply(project:Project):Unit = {
    val extension = new Extension(project)
    project.getExtensions.add("unison", extension)
    tasks.create(project, extension)
  }
}


object UnisonGradlePlugin {
  class Extension(project:Project) extends UnisonExtensionApi(project) {
    @Nullable @BeanProperty var login:String = _
    @Nullable @BeanProperty var password:String = _

    @Nullable @BeanProperty var roomID:CharSequence = _
    def roomID(id:CharSequence):Unit = { this.roomID = id }
    @Nullable @BeanProperty var topicID:CharSequence = _
    def topicID(id:CharSequence):Unit = { this.topicID = id }
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

    // used for testing
    private[plugin] var clientFactory:(String, String) ⇒ UnisonApi = UnisonApiClient.create
  }
}
