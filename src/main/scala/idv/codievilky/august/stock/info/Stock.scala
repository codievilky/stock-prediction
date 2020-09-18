package idv.codievilky.august.stock
package info

import idv.codievilky.august.common.Season.Season
import idv.codievilky.august.stock.analyse.PossibleValue

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class Stock(val stockInfo: StockInfo, val price: StockPrice, val financialSituation: FinancialSituation) {

  def peOf(seasonInfo: SeasonInfo): Double = {
    Stock.calcPe(priceOf(seasonInfo) * totalShares, netProfitOf(seasonInfo))
  }

  def totalShares = stockInfo.totalShares

  def priceOf(seasonInfo: SeasonInfo) = price.getStockPriceOf(seasonInfo) match {
    case Some(stockDayPrice) => stockDayPrice.calcPrice
    case None => throw new IllegalArgumentException("can not find price of season: " + seasonInfo)
  }

  def netProfitOf(seasonInfo: SeasonInfo) = financialSituation.netProfit(seasonInfo)

  // 计算今年可能的利润
  def guessPossibleProfit(calcSeason: SeasonInfo, calculateStartYear: Int): List[PossibleValue] = {
    financialSituation.guessByYearIncrease(calcSeason, calculateStartYear) :: financialSituation.guessBySeasonIncrease(calcSeason)
  }

  def guessPossiblePe(calcSeason: SeasonInfo, calculateStartYear: Int) = {
    val peList = for (season <- SeasonInfo(calculateStartYear, Season.Q1) until calcSeason) yield peOf(season)
    PossibleValue(peList.max, 0.40) :: PossibleValue(peList.min, 0.40) :: PossibleValue(peList.sum / peList.length, 0.80) :: Nil
  }

  // 通过估计值，计算股票可能的价格
  def calcPossiblePrice(startSeason: SeasonInfo, targetSeason: SeasonInfo, possibleProfit: Long, possiblePe: Double): Double = {
    var capitalization: Double = price(startSeason).calcPrice * totalShares
    // 计算出目前季度的上个记录的可能价格
    for (season <- startSeason until targetSeason) {
      capitalization += ((netProfitOf(season) - netProfitOf(season.lastYear)) * peOf(season))
    }
    (capitalization + (possibleProfit - netProfitOf(targetSeason.lastYear)) * possiblePe) / totalShares
  }
}

object Stock extends App {
  def calcPe(capitalization: Double, netProfit: Long): Double = capitalization / netProfit
}

case class StockCode(code: String) {
  val fullCode = if (code.startsWith("0") || code.startsWith("300")) s"sz$code"
  else if (code.startsWith("6")) s"sh$code"
  else throw new IllegalArgumentException(s"unknown stock code $code")
}
