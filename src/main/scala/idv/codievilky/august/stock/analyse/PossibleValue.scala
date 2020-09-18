package idv.codievilky.august.stock
package analyse

import idv.codievilky.august.stock.info.SeasonInfo

/**
 * @auther Codievilky August
 * @since 2020/9/15
 */
case class PossibleValue(value: Number, percentage: Double) {

}

class PossiblePrice(val price: Double,
                    val possibleProfit: PossibleValue,
                    val possiblePe: PossibleValue,
                    val seasonInfo: SeasonInfo
                   ) extends PossibleValue(price, keepNumber(possiblePe.percentage * possibleProfit.percentage, 4)) {
  override def toString = s"at $seasonInfo, (price:${keepNumber(price)}, pe:${possiblePe.value.longValue()}, possibleProfit:${possibleProfit.value}, possibility:$percentage)"

  def compareTo(o: PossiblePrice) = {
    price.compareTo(o.price)
  }
}
