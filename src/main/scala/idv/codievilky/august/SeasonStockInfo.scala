package idv.codievilky.august


/**
 * @auther Codievilky August
 * @since 2020/9/2
 */


object Season extends Enumeration {
  type Season = Value
  val Q1, Q2, Q3, Q4 = Value
}

package xx {

  import idv.codievilky.august.Season.Season

  class SeasonStockInfo(price: Double, eps: Double, cfps: Double, year: Int, season: Season) {

  }

}

trait a[A, B] {
  def returnOption(): (Option[A], Option[B])
}

class august {
  def august(test: a[_, _]): Unit = {
    val (ra, rb) = test.returnOption()
    ra.foreach(_ => print("success"))
  }
}


object TestMain {

  import idv.codievilky.august.xx.SeasonStockInfo

  def main(args: Array[String]): Unit = {
    new SeasonStockInfo(0, 0, 0, 2019, Season.Q2)

  }
}

class X {

  import idv.codievilky.august.xx._

  def x: SeasonStockInfo = new SeasonStockInfo(0, 0, 0, 2019, Season.Q1)
}

