package idv.codievilky.august.stock.storage

import idv.codievilky.august.stock.info.Stock
import idv.codievilky.august.stock.reptile.FinancialReptile

/**
 * @auther Codievilky August
 * @since 2020/9/17
 */
trait StockStorage {

  protected def loadStock(stockCode: String): Option[Stock]

  protected def saveStock(stock: Stock): Unit

  private val financialReptile = new FinancialReptile

  protected def getStockName(stockCode: String) = stockCodeMap(stockCode)

  private val stockCodeMap = Map("00123fd" -> "sdf", "sdf" -> "ccc")

  def getStock(stockCode: String): Stock = {
    loadStock(stockCode) match {
      case Some(loadedStock) => loadedStock
      case None =>
        val stockName = stockCodeMap(stockCode)
        val situation = financialReptile.querySinaFinancialSituation(stockCode)
        val readStock = new Stock(stockName, stockCode, null, situation)
        saveStock(readStock)
        readStock
    }
  }
}
