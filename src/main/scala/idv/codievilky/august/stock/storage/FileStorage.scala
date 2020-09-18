package idv.codievilky.august.stock
package storage

import java.io.File

import grizzled.slf4j.Logger
import idv.codievilky.august.stock.info._
import org.apache.commons.io.FileUtils

/**
 * @auther Codievilky August
 * @since 2020/9/13
 */
object FileStorage extends StockStorage {
  val log = Logger[this.type]()
  def saveStock(stock: Stock): Unit = {
    val file = new File("/Users/codievilky/ca/stock-prediction/src/main/resources", s"${stock.stockInfo.stockName}.json")
    FileUtils.writeStringToFile(file, JSON_WRITER.writeValueAsString(stock), "UTF-8")
  }

  def loadStock(stockCode: String): Option[Stock] = {
    val stockName = getStockName(stockCode)
    val file = new File("/Users/codievilky/ca/stock-prediction/src/main/resources", s"${stockName}.json")
    if (file.exists()) {
      val stockInfo = FileUtils.readFileToString(file, "UTF-8")
      log.info("succeed to load config")
      Some(DEFAULT_OBJECT_MAPPER.readValue(stockInfo, classOf[Stock]))
    } else None
  }
}
