package org.github.dant3

import org.codehaus.groovy.control.ConfigurationException
import org.gradle.api.Nullable

import scala.beans.BeanProperty

class UnisonGradleExtension {
  @Nullable @BeanProperty var login:String = _
  @Nullable @BeanProperty var password:String = _

  def login(login:String):Unit = this.login = login
  def password(password:String):Unit = this.password = password

  private[dant3] def getCredentials:(String, String) = if (login == null || password == null) {
    throw new ConfigurationException(noLoginPasswordError)
  } else {
    (login, password)
  }

  private val noLoginPasswordError =
      """Either login or password for unison connection was not provided.
        |This can be fixed by adding configuration to your build:
        |
        |unison {
        |    login "myLogin"
        |    password "myPassword"
        |}
      """.stripMargin
}
