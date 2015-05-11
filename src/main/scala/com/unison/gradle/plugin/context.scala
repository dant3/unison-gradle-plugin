package com.unison.gradle.plugin

import org.codehaus.groovy.control.ConfigurationException

case class Context(ext:UnisonGradlePlugin.Configuration) {
  def roomID = ext.roomID
  def topicID = ext.topicID
  def commentText = ext.commentText

  private[plugin] def createClient = ext.clientFactory.tupled.apply(getCredentials)
  private[plugin] def getCredentials:(String, String) = (ext.login, ext.password) match {
    case (l, p) if l == null || p == null ⇒
      throw new ConfigurationException(noLoginPasswordError)
    case validLoginPassword ⇒
      validLoginPassword
  }
}
