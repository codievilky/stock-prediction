package idv.codievilky.august.stock
package storage

import java.io.File

import idv.codievilky.august.stock.info._
import org.apache.commons.io.FileUtils

/**
 * @auther Codievilky August
 * @since 2020/9/13
 */
object FileStorage extends StockStorage {

  def saveStock(stock: Stock): Unit = {
    val file = new File("/Users/codievilky/ca/stock-prediction/src/main/resources", s"${stock.stockName}.json")
    FileUtils.writeStringToFile(file, JSON_WRITER.writeValueAsString(stock), "UTF-8")
  }

  def loadStock(stockCode: String): Option[Stock] = {
    val stockName = getStockName(stockCode)
    val file = new File("/Users/codievilky/ca/stock-prediction/src/main/resources", s"${stockName}.json")
    if (file.exists()) {
      val stockInfo = FileUtils.readFileToString(file, "UTF-8")
      Some(DEFAULT_OBJECT_MAPPER.readValue(stockInfo, classOf[Stock]))
    } else None
  }
}
