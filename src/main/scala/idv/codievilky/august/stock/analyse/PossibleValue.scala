package idv.codievilky.august.stock
package analyse

/**
 * @auther Codievilky August
 * @since 2020/9/15
 */
case class PossibleValue(value: Number, percentage: Double) {

}

class PossiblePrice(val price: Double, val possibleProfit: PossibleValue, val possiblePe: PossibleValue) extends PossibleValue(price, keepNumber(possiblePe.percentage * possibleProfit.percentage, 4)) {
  override def toString = s"(price:${keepNumber(price)}, pe:${possiblePe.value.longValue()}, profit:${possibleProfit.value}, possibility:$percentage)"

  def compareTo(o: PossiblePrice) = {
    price.compareTo(o.price)
  }
}
