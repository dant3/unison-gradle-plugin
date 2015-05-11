package com.unison.gradle

package object plugin {
  val noLoginPasswordError =
    """Either login or password for unison connection was not provided.
      |This can be fixed by adding configuration to your build:
      |
      |unison {
      |    login "myLogin"
      |    password "myPassword"
      |}
    """.stripMargin
}
