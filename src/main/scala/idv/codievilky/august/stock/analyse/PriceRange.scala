package idv.codievilky.august.stock.analyse

import grizzled.slf4j.Logger

/**
 * @auther Codievilky August
 * @since 2020/9/14
 */
class PriceRange(outerPossiblePriceList: List[PossiblePrice]) {
  private val log = Logger[this.type]()
  val possiblePriceList = outerPossiblePriceList.sortBy(_.percentage).reverse
  val maxPrice = outerPossiblePriceList.iterator.maxBy(_.price).price
  val minPrice = outerPossiblePriceList.minBy(_.price).price

  override def toString = s"the max price of stock is $maxPrice and the min price of stock is $minPrice"

  def print(): Unit = {
    log.info(toString)
    possiblePriceList.foreach(log.info(_))
  }
}
