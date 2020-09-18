package idv.codievilky.august.stock
package analyse

import grizzled.slf4j.Logger
import idv.codievilky.august.common.Season.Season
import idv.codievilky.august.stock.info.{SeasonInfo, Stock}
import idv.codievilky.august.stock.storage.FileStorage

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
object StockAnalyzer {
  private val log = Logger[this.type]()

  def printStock(stock: Stock): Unit = {}

  def calcPossiblePriceOf(targetSeason: SeasonInfo, stock: Stock, calcStartYear: Int): PriceRange = {
    // 首先必须至少有两年的数据才可以计算
    stock.financialSituation.allFinancialInfo.get(targetSeason.lastYear) match {
      case None => val msg = s"not enough data at season of $targetSeason"
        log.info(msg)
        throw new IllegalArgumentException()
      case _ =>
    }
    val startSeason = targetSeason.lastSeason
    // 估计本季度可能的净利润
    val possibleProfitSet = stock.guessPossibleProfit(targetSeason, calcStartYear)
    val possiblePeSet = stock.guessPossiblePe(targetSeason, calcStartYear)
    // 计算出目前季度的上个记录的可能价格
    val possiblePrice = for (profit <- possibleProfitSet; pe <- possiblePeSet) yield {
      val price = stock.calcPossiblePrice(startSeason, targetSeason, profit.value.longValue(), pe.value.doubleValue())
      new PossiblePrice(price, profit, pe)
    }
    new PriceRange(possiblePrice.toList)
  }
}

object Analyzer extends App {
  val stock = FileStorage.getStock("002488")
  //  StockAnalyzer.calcPossiblePriceOf(SeasonInfo(2020, Season.Q3), stock, 2018).print()
  StockAnalyzer.calcPossiblePriceOf(SeasonInfo(2020, Season.Q3), stock, 2019).print()

}