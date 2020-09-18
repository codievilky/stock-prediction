package idv.codievilky.august.stock
package reptile

import java.util.Calendar

import grizzled.slf4j.Logger
import idv.codievilky.august.stock.info.{DayInfo, SeasonInfo, StockCode, StockPrice}

import scala.collection.mutable

/**
 * 年份都是 20 开头。只能用于 21 世纪
 *
 * @auther Codievilky August
 * @since 2020/9/5
 */
object TencentPriceReptile {
  private val log = Logger[this.type]()
  val THE_EARLIEST_YEAR = 2016

  def queryStockPrice(stockCode: String): StockPrice = {
    val currentYear = SeasonInfo.chinaCalendar.get(Calendar.YEAR)
    val allPrice = new mutable.HashMap[DayInfo, Double]()
    val fullStockCode = StockCode(stockCode).fullCode
    for (year <- THE_EARLIEST_YEAR to currentYear)
      queryPriceByYear(fullStockCode, year % 100) match {
        case Some(priceMap) =>
          log.info(s"loaded the price of $year.")
          allPrice ++= priceMap

        case _ =>
      }
    new StockPrice(currentYear, allPrice)
  }

  def queryPriceByYear(fullStockCode: String, cutYear: Int): Option[mutable.HashMap[DayInfo, Double]] = {
    val response = doGet(s"http://data.gtimg.cn/flashdata/hushen/daily/$cutYear/$fullStockCode.js")
    if (response == null) {
      return None
    }
    val result = response.linesIterator.foldLeft(new mutable.HashMap[DayInfo, Double]()) { (m, line) =>
      // line date         收盘价
      //      200102 33.01 36.09 36.09 32.84 35356\n\
      val splitResult = line.split(' ')
      if (splitResult.length > 2) {
        val date = splitResult(0)
        val price = splitResult(2).toDouble
        m += (DayInfo(2000 + cutYear, date.substring(2, 4).toInt, date.substring(4, 6).toInt) -> price)
      } else m
    }
    Some(result)
  }

  def main(args: Array[String]): Unit = {
    println(queryStockPrice("601899"))
  }
}
