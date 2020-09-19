package idv.codievilky.august.stock
package reptile

import idv.codievilky.august.stock.info.{DayInfo, StockCode}

import scala.collection.mutable

/**
 * 年份都是 20 开头。只能用于 21 世纪
 *
 * @auther Codievilky August
 * @since 2020/9/5
 */
object TencentPriceReptile extends PriceReptile {

  def queryPriceByYear(stockCode: StockCode, year: Int): Option[mutable.HashMap[DayInfo, Double]] = {
    innerQueryPriceByYear(stockCode.fullCode, year % 100)
  }

  def innerQueryPriceByYear(fullStockCode: String, cutYear: Int): Option[mutable.HashMap[DayInfo, Double]] = {
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
