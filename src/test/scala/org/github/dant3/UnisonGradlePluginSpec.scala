package org.github.dant3

import org.gradle.testfixtures.ProjectBuilder
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, WordSpec}

@RunWith(classOf[JUnitRunner])
class UnisonGradlePluginSpec extends WordSpec with Matchers {
  "UnisonGradlePlugin" should {
    "be unable to apply" in {
      an [NotImplementedError] should be thrownBy {
        val project = ProjectBuilder.builder.build()
        project.getPlugins.apply("org.github.dant3.unison-plugin")
      }
    }
  }
}
