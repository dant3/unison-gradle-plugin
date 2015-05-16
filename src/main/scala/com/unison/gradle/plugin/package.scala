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

  val noCommentText =
    """No comment text is supplied. You can configure comment text to post in your build:
      |
      |unison {
      |    commentText {
      |        // your closure here, which evaluates to String which will be posted to Unison
      |    }
      |    // or alternatively for statical comment:
      |    // commentText "myText"
      |}
      |
    """.stripMargin

  val noRoomIDError = extParamRequired("room ID", "roomID", "01234567890")
  val noTopicIDError = extParamRequired("topic ID", "topicID", "01234567890")

  def extParamRequired(paramName:String, paramKey:String, exampleValue:String, externalParamKey:Option[String] = None) = {
    val extParamKey = externalParamKey.getOrElse(paramKey)
    s"""${paramName.capitalize} is not provided. You can provide $paramName in your build:
       |
       |unison {
       |    $paramKey = "$exampleValue"
       |}
       |
       |or using external properties:
       |
       |ext {
       |    $extParamKey = "$exampleValue"
       |}
       |
       |Or alternatively you can supply them during gradle call:
       |
       |gradle <unisonTask> -D$extParamKey=$exampleValue
     """.stripMargin
  }
}
