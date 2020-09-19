package idv.codievilky.august.stock.storage

import grizzled.slf4j.Logger
import idv.codievilky.august.stock.info.Stock
import idv.codievilky.august.stock.reptile.{PriceReptile, SinaFinancialReptile, SinaStockInfoReptile}

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/17
 */
trait StockStorage {

  protected def loadStock(stockCode: String): Option[Stock]

  protected def saveStock(stock: Stock): Unit

  protected def getStockName(stockCode: String) = {
    stockCodeMap.get(stockCode) match {
      case Some(stockName) => stockName
      case _ =>
        val stockInfo = SinaStockInfoReptile.queryStockInfo(stockCode)
        stockCodeMap(stockCode) = stockInfo.stockName
        stockInfo.stockName
    }
  }

  private val stockCodeMap = mutable.Map[String, String]()
  private val log = Logger[this.type]()

  def getStock(stockCode: String): Stock = {
    if (stockCode.length != 6) {
      throw new IllegalArgumentException(s"illegal code length: ${stockCode.length}")
    }
    val stock = loadStock(stockCode) match {
      case Some(loadedStock) => loadedStock
      case None =>
        val situation = SinaFinancialReptile.querySinaFinancialSituation(stockCode)
        val stockInfo = SinaStockInfoReptile.queryStockInfo(stockCode)
        stockCodeMap(stockCode) = stockInfo.stockName
        val stockPrice = PriceReptile.currentReptile.queryStockPrice(stockCode)
        val readStock = new Stock(stockInfo, stockPrice, situation)
        saveStock(readStock)
        readStock
    }
    log.info(s"you have get the stock of name: ${stock.stockInfo.stockName}")
    stock
  }
}
