package idv.codievilky.august.stock
package info

import com.fasterxml.jackson.annotation.JsonProperty
import grizzled.slf4j.Logger
import idv.codievilky.august.common.Season.Season

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class StockPrice {
  private val log = Logger[this.type]()
  // 认为季度的平均价格，即，几日均线作为平均
  private val AVERAGE_PRICE_DAY_NUM = 10
  val seasonPriceMap = new mutable.HashMap[SeasonInfo, StockDayPrice]()
  private var innerMaxSeason: SeasonInfo = _

  def maxSeason = {
    if (innerMaxSeason == null) innerMaxSeason = seasonPriceMap.keySet.max
    innerMaxSeason
  }

  def init(currentYear: Int, allPrice: mutable.HashMap[DayInfo, Double]): StockPrice = {
    val realStartDay = allPrice.keySet.minBy(_.toInt)
    for (season <- SeasonInfo(realStartDay.year, Season.Q1) until SeasonInfo(currentYear + 1, Season.Q1)) {
      val lastDayOfSeason = season.lastDayOfSeason
      var foundPriceNum = 0
      var priceSum: Double = 0
      var queryDay = lastDayOfSeason
      while (foundPriceNum < AVERAGE_PRICE_DAY_NUM && queryDay >= realStartDay) {
        allPrice.get(queryDay) match {
          case Some(foundedPrice) =>
            foundPriceNum += 1
            priceSum += foundedPrice
          case _ =>
        }
        queryDay -= 1
      }
      if (foundPriceNum > 0) {
        val dayPrice = new StockDayPrice(priceSum / foundPriceNum)
        log.info(s"loaded $season price at ${dayPrice.calcPrice}.")
        seasonPriceMap += (season -> dayPrice)
        innerMaxSeason = if (season > innerMaxSeason) season else innerMaxSeason
      }
    }
    this
  }


  def getStockPriceOf(seasonInfo: SeasonInfo): Option[StockDayPrice] = seasonPriceMap.get(seasonInfo)

  def apply(seasonInfo: SeasonInfo): Option[StockDayPrice] = {
    if (seasonInfo > maxSeason) throw new IndexOutOfBoundsException("season has exceed the max season")
    seasonPriceMap.get(seasonInfo)
  }
}

class StockDayPrice(@JsonProperty("calc_price")
                    realPrice: Double) {

  // 保留两位小数
  val calcPrice = (realPrice * 100).toLong.toDouble / 100

  override def toString = s"price:$calcPrice"
}
