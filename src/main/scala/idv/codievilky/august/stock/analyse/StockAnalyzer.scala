package idv.codievilky.august.stock
package analyse

import idv.codievilky.august.stock.info.{SeasonInfo, Stock}
import idv.codievilky.august.stock.storage.FileStorage

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
object StockAnalyzer {
  def printStock(stock: Stock): Unit = {}

  def calcPossiblePriceOf(targetSeason: SeasonInfo, stock: Stock, calcStartYear: Int): PriceRange = {
    val startSeason = SeasonInfo(calcStartYear, Season.Q1)
    // 估计本季度可能的净利润
    val possibleProfitList = stock.guessPossibleProfit(targetSeason, calcStartYear)
    val possiblePeList = stock.guessPossiblePe(targetSeason, calcStartYear)
    // 计算出目前季度的上个记录的可能价格
    val possiblePrice = for (season <- startSeason until targetSeason; profit <- possibleProfitList; pe <- possiblePeList) yield {
      val price = stock.calcPossiblePrice(season, targetSeason, profit.value.longValue(), pe.value.doubleValue()).toLong
      new PossiblePrice(price, profit, pe)
    }
    new PriceRange(possiblePrice.toList)
  }
}

object Analyzer extends App {
  val stock = FileStorage.getStock("601788")
  //  StockAnalyzer.calcPossiblePriceOf(SeasonInfo(2020, Season.Q3), stock, 2018).print()
  StockAnalyzer.calcPossiblePriceOf(SeasonInfo(2020, Season.Q2), stock, 2019).print()
}