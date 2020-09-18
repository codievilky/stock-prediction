package idv.codievilky.august.stock.info

/**
 * @auther Codievilky August
 * @since 2020/9/17
 */
case class StockInfo(stockName: String, stockCode: String, totalShares: Long) {
  val fullStockCode = StockCode(stockCode).fullCode
}
