package idv.codievilky.august.stock.info

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class StockPrice(val totalSharesValue: Long) {
  def totalShares = totalSharesValue * 1_0000_0000

  val seasonPriceMap = new mutable.HashMap[SeasonInfo, StockDayPrice]()

  def getStockPriceOf(seasonInfo: SeasonInfo): Option[StockDayPrice] = seasonPriceMap.get(seasonInfo)

  def apply(seasonInfo: SeasonInfo) = seasonPriceMap(seasonInfo)
}

class StockDayPrice(val calcPrice: Long) {
  override def toString = s"price:$calcPrice"
}

object PriceHelper {
  def toDisplayPrice(price: Long) = price.toDouble / 100
}
