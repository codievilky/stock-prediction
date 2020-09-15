package idv.codievilky.august.stock
package storage

import java.io.File

import idv.codievilky.august.stock.info._
import org.apache.commons.io.FileUtils

/**
 * @auther Codievilky August
 * @since 2020/9/13
 */
object FileStorage {

  def save(stock: Stock): Unit = {
    val file = new File("/Users/codievilky/ca/stock-prediction/src/main/resources", s"${stock.stockName}.json")
    FileUtils.writeStringToFile(file, JSON_WRITER.writeValueAsString(stock), "UTF-8")
  }

  def read(stockName: String): Stock = {
    val file = new File("/Users/codievilky/ca/stock-prediction/src/main/resources", s"${stockName}.json")
    val stockInfo = FileUtils.readFileToString(file, "UTF-8")
    DEFAULT_OBJECT_MAPPER.readValue(stockInfo, classOf[Stock])
  }

  def main(args: Array[String]): Unit = {
    val si = new FinancialSituation
    si += SeasonInfo(2020, Season.Q1) -> new FinancialInfo(1, 2, 3)
    val abcStock = new Stock("abc", 2312, new StockPrice(10), si);
    abcStock.price.seasonPriceMap += (SeasonInfo(2011, Season.Q2) -> new StockDayPrice(10))
    save(abcStock)
    val readStock = read("abc")
    println(readStock.financialSituation.allFinancialInfo.iterator.next()._1)
  }


}
