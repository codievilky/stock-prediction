package idv.codievilky.august.stock.analyse

import java.util.Comparator

/**
 * @auther Codievilky August
 * @since 2020/9/14
 */
class PriceRange(outerPossiblePriceList: List[PossiblePrice]) {
  val possiblePriceList = outerPossiblePriceList.sortBy(_.percentage).reverse
  val maxPrice = outerPossiblePriceList.iterator.max((a: PossiblePrice, b: PossiblePrice) => a.price.compareTo(b.price))
  val minPrice = outerPossiblePriceList.min((a: PossiblePrice, b: PossiblePrice) => a.price.compareTo(b.price))




  override def toString = s"the max price of stock is 10 and the min price of stock is 12"

  def print(): Unit = {
    println(toString)
    println(possiblePriceList.mkString("\n"))
  }
}
