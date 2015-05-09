package org.github.dant3

import org.gradle.api.Action

object GradleConversions {
  implicit def action[T](function: T ⇒ Any):Action[T] = new Action[T] {
    override def execute(t: T): Unit = function.apply(t)
  }
}
