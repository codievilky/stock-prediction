package idv.codievilky.august.stock.reptile

import com.fasterxml.jackson.databind.node.ArrayNode
import idv.codievilky.august.stock.info.{DayInfo, StockCode}
import idv.codievilky.august.stock.{DEFAULT_OBJECT_MAPPER, doGet}

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/19
 */
object EastMoneyPriceReptile extends PriceReptile {

  def queryPriceByYear(stockCode: StockCode, year: Int): Option[mutable.HashMap[DayInfo, Double]] = {
    val prefix = if (stockCode.inSz) 0 else 1
    val response = doGet(s"http://98.push2his.eastmoney.com/api/qt/stock/kline/get?secid=$prefix.${stockCode.code}&fields1=f5&fields2=f51,f53&klt=101&fqt=1&end=${year + 1}0101&lmt=250")
    if (response == null) {
      return None
    }
    try {
      val dataNode = DEFAULT_OBJECT_MAPPER.readTree(response).get("data").get("klines").asInstanceOf[ArrayNode]
      val priceIterator = dataNode.iterator()
      val result = new mutable.HashMap[DayInfo, Double]()
      while (priceIterator.hasNext) {
        val priceInfo = priceIterator.next().asText()
        val splitData = priceInfo.split(',')
        val dayInfo = DayInfo.fromDate(splitData(0))
        result += (dayInfo -> splitData(1).toDouble)
      }
      Some(result)
    } catch {
      case e: Exception => log.warn("parse response failed.", e)
        None
    }
  }


  def main(args: Array[String]): Unit = {
    println(EastMoneyPriceReptile.queryStockPrice("000661"))
  }

}
