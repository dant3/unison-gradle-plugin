package com.unison.gradle.plugin

import com.unison.api.UnisonApi
import com.unison.api.client.UnisonApiClient
import com.unison.gradle.plugin.UnisonGradlePlugin.Configuration
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


object UnisonGradlePlugin {
    class Configuration {
        @Nullable @BeanProperty var login:String = _
        @Nullable @BeanProperty var password:String = _

        def roomID = ???
        def topicID = ???
        def commentText = ???

        def login(login:String):Unit = this.login = login
        def password(password:String):Unit = this.password = password

        private[plugin] var clientFactory:(String, String) ⇒ UnisonApi = UnisonApiClient.create
        private[plugin] def createClient = clientFactory.tupled.apply(getCredentials)
        private[plugin] def getCredentials:(String, String) = if (login == null || password == null) {
            throw new ConfigurationException(noLoginPasswordError)
        } else {
            (login, password)
        }
    }
}