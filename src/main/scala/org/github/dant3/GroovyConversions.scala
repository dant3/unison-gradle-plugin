package org.github.dant3

import groovy.lang.Closure

import scala.language.implicitConversions

trait GroovyConversions {
  implicit def toGroovyClosure[O](block: ⇒ O):Closure[O] = new Closure[O](null) {
    def doCall(): O = block
  }

  implicit def toGroovyClosure[I,O](fn: I ⇒ O):Closure[O] = new Closure[O](fn) {
    def doCall(arg:I): O = fn(arg)
  }
}

object GroovyConversions extends GroovyConversions
