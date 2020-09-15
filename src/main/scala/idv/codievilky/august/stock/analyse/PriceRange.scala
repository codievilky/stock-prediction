package idv.codievilky.august.stock.analyse

import idv.codievilky.august.stock.info.PriceHelper

/**
 * @auther Codievilky August
 * @since 2020/9/14
 */
class PriceRange(val possiblePriceList: List[PossibleValue]) {
  private val priceList = for (p <- possiblePriceList) yield p.value.longValue()


  override def toString = s"the max price of stock is 10 and the min price of stock is 12"

  def print(): Unit = {
    println(toString)
    println(possiblePriceList.sortBy(_.percentage).reverse)
  }
}
