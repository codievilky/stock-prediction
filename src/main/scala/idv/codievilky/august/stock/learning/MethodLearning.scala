package idv.codievilky.august.stock.learning

/**
 * @auther Codievilky August
 * @since 2020/9/7
 */
class MethodLearning {

  def triple(x: Double) = 3 * x

  val at = triple _

  println(at(10))
}


object MethodMain extends App {
  val m = new MethodLearning
  
}
