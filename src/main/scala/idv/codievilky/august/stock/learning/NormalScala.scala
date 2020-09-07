package idv.codievilky.august.stock.learning

import scala.Console.println
import scala.reflect.ClassTag.Nothing

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class NormalScala {


}

object NormalMain {
  def main(args: Array[String]): Unit = {
    val score = collection.mutable.Map("ab" -> 2, "cd" -> 4)
    val newScore = (score ++ collection.mutable.Map("fb" -> 5, "cd" -> 2))
    newScore.update("cd", 2)
    println(newScore)
  }
}
