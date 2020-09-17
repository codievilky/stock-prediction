package idv.codievilky.august.stock.learning

import scala.annotation.tailrec

/**
 * @auther Codievilky August
 * @since 2020/9/16
 */
object FunctionLearning extends App {

  @tailrec
  def until(condition: => Boolean)(block: => Unit): Unit = {
    if (!condition) {
      block
      until(condition)(block)
    }
  }

  def threeParam(a: Int)(b: Int)(c: Int)(x: => Unit) = {
    println(x)
    a * b * c * (_: Int)
  }

  threeParam(1)(2)(3)(println("de"))

  def indexOf(str: String, ch: Char): Int = {
    val x = () => {
      val b = () => return 12
      def c: Int = b()
      println(s"inner $c")
    }
    if (str.length > 10) {
      x()
      10
    } else {
      11
    }
  }

  println(indexOf("abcdddddddddddddddddddd", 'c'))

  var x = 10
  until(x == 10) {
    x -= 1
    println(x)
  }
}
