package idv.codievilky.august.stock.learning

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class NormalScala {

}

object NormalMain {
  def main(args: Array[String]): Unit = {
    val s = Seq(1, 4, 3, 5, 6, 7, "s")
    println(s.map {
      case i: Int => i * 10
      case v:String => s"10$v"
    })
    println(s.collect {
      case i: Int => i * 10
      case b: Long => b * 100
    })
    println(s.flatMap(i => Some(10)))

  }
}
