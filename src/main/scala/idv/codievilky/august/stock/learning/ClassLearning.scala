package idv.codievilky.august.stock
package learning

import scala.beans.BeanProperty
import scala.math.ceil

/**
 * @auther Codievilky August
 * @since 2020/9/6
 */
class ClassLearning(xx: Int) {
  outer =>
  @BeanProperty var page = 0;

  def this(int: String) = {
    this(int.toInt)
    page = xx
  }

  class InnerClass {
    def getPage: Int = ClassLearning.this.page

    def getOuter = outer
  }

  var inner = new InnerClass

  @BeanProperty protected[stock] val s = 10

  def age: Int = xx

  def age_=(newAge: Int): Unit = if (newAge > 100) page = newAge

  def tempS(): Int = xx

}

class BExtend(xb: Int) extends ClassLearning(xb) {
  override val s = {
    println("s is" + super.getS())
    tempS()
  }
  val b = 10

  override var age = xb

  def `!3bdf`(int: Int) = s

  val unary_~ : Int = {
    println("august"); 10
  }

  val fun = ceil _
}

object MyUnP {
  def unapplySeq(arg: String):Option[Seq[Int]] = {
    if (arg == null) None
    else if ("a" == arg) Some(Array(1,2).toIndexedSeq)
    else Some(Array(1,2,3).toIndexedSeq)
  }
}

object Name {
  def unapply(name: String): Option[(String, String)] = {
    val pos = name.indexOf(" ")
    if (pos == -1) None
    else Some((name.substring(0, pos), name.substring(pos+1)))
  }
}

object IsCompound {
  def unapply(input: String) = input.contains(" ")
}

object Main extends App {
  val c = new ClassLearning("10")
  val b = new ClassLearning("20")
  val s = new BExtend(20)
  val √ = scala.math.pow _
  val `!#.;2-12` = 10
  val author = "temp black d"
  author match {
    case Name(first, last @ IsCompound()) => println(last)
    case Name(first, last) => println(first)
  }

  author match {
    case MyUnP(a,b) => println(a)
    case MyUnP(a,b,c) => println(c)
  }


}