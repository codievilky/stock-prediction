package idv.codievilky.august.stock
package analyse

import idv.codievilky.august.stock.info.PriceHelper

/**
 * @auther Codievilky August
 * @since 2020/9/15
 */
case class PossibleValue(value: Number, percentage: Double) {

}

class PossiblePrice(val price: Long, val possibleProfit: PossibleValue, val possiblePe: PossibleValue) extends PossibleValue(price, keepNumber(possiblePe.percentage * possibleProfit.percentage, 4))  {
  override def toString = s"(price:${PriceHelper.toDisplayPrice(price)}, pe:${PriceHelper.toDisplayPrice(possiblePe.value.longValue())}, profit:${possibleProfit.value}, possibility:$percentage)"

   def compareTo(o: PossiblePrice) = {
    price.compareTo(o.price)
  }
}
