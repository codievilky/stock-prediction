package idv.codievilky.august.stock.reptile

import java.util.Calendar

import grizzled.slf4j.Logger
import idv.codievilky.august.stock.info.{DayInfo, SeasonInfo, StockCode, StockPrice}

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/19
 */
trait PriceReptile {
  protected val log = Logger[this.type]()
  val THE_EARLIEST_YEAR = 2016

  def queryStockPrice(stockCodeStr: String): StockPrice = {
    val currentYear = SeasonInfo.chinaCalendar.get(Calendar.YEAR)
    val allPrice = new mutable.HashMap[DayInfo, Double]()
    val stockCode = StockCode(stockCodeStr)
    for (year <- THE_EARLIEST_YEAR to currentYear)
      queryPriceByYear(stockCode, year) match {
        case Some(priceMap) =>
          log.info(s"loaded the price of $year.")
          allPrice ++= priceMap
        case _ =>
      }
    new StockPrice(currentYear, allPrice)
  }

  def queryPriceByYear(stockCode: StockCode, year: Int): Option[mutable.HashMap[DayInfo, Double]]
}

object PriceReptile {
  def currentReptile: PriceReptile = EastMoneyPriceReptile
}


