/*
rule = AuxRule
 */
package fix

import scala.annotation.StaticAnnotation

object Scalafixdemo {
  @aux
  class A {
    type T
  }
}

class aux extends StaticAnnotation
