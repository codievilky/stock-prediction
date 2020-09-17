package idv.codievilky.august.stock
package reptile

import idv.codievilky.august.stock.info.{StockCode, StockInfo}

/**
 * @auther Codievilky August
 * @since 2020/9/17
 */
object SinaStockInfoReptile {

  def queryStockInfo(stockCode: String): StockInfo = {
    val fullStockCode = StockCode(stockCode).fullCode
    val response = doGet(s"http://hq.sinajs.cn/rn=1600350085787&list=${fullStockCode}_i")
    val contentArray = response.substring(response.indexOf('"') + 1, response.lastIndexOf('"')).split(',')
    StockInfo(contentArray(22), stockCode, (contentArray(7).toDouble * 1000).toLong)
  }

  def main(args: Array[String]): Unit = {
    println(queryStockInfo("000002"))
  }
}
