package idv.codievilky.august.stock.storage

import idv.codievilky.august.stock.info.Stock
import idv.codievilky.august.stock.reptile.{SinaFinancialReptile, SinaStockInfoReptile, TencentPriceReptile}

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/17
 */
trait StockStorage {

  protected def loadStock(stockCode: String): Option[Stock]

  protected def saveStock(stock: Stock): Unit

  protected def getStockName(stockCode: String) = stockCodeMap.getOrElse(stockCode, "UNKNOWN")

  private val stockCodeMap = mutable.Map("00123fd" -> "sdf", "sdf" -> "ccc")

  def getStock(stockCode: String): Stock = {
    loadStock(stockCode) match {
      case Some(loadedStock) => loadedStock
      case None =>
        val situation = SinaFinancialReptile.querySinaFinancialSituation(stockCode)
        val stockInfo = SinaStockInfoReptile.queryStockInfo(stockCode)
        stockCodeMap(stockCode) = stockInfo.stockName
        val stockPrice = TencentPriceReptile.queryStockPrice(stockCode)
        val readStock = new Stock(stockInfo, stockPrice, situation)
        saveStock(readStock)
        readStock
    }
  }
}
