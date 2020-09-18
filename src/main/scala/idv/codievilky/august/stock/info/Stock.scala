package idv.codievilky.august.stock
package info

import grizzled.slf4j.Logger
import idv.codievilky.august.common.Season.Season
import idv.codievilky.august.stock.analyse.PossibleValue

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class Stock(val stockInfo: StockInfo, val price: StockPrice, val financialSituation: FinancialSituation) {
  private val log = Logger[this.type]()

  def peOf(seasonInfo: SeasonInfo): Double = {
    Stock.calcPe(priceOf(seasonInfo), netProfitByShareOf(seasonInfo))
  }

  def totalShares = stockInfo.totalShares

  def priceOf(seasonInfo: SeasonInfo) = price.getStockPriceOf(seasonInfo) match {
    case Some(stockDayPrice) => stockDayPrice.calcPrice
    case None => throw new IllegalArgumentException(s"can not find price of season: $seasonInfo")
  }

  private val netProfitByShareCache = new mutable.HashMap[SeasonInfo, Double]()

  def netProfitByShareOf(seasonInfo: SeasonInfo): Double = {
    netProfitByShareCache.getOrElseUpdate(seasonInfo, financialSituation.netProfit(seasonInfo).toDouble / totalShares)
  }

  // 计算今年可能的利润
  def guessPossibleProfit(calcSeason: SeasonInfo, calculateStartYear: Int): Set[PossibleValue] = {
    financialSituation.guessBySeasonIncrease(calcSeason) + financialSituation.guessByYearIncrease(calcSeason, calculateStartYear)
  }

  def guessPossiblePe(calcSeason: SeasonInfo, calculateStartYear: Int): Set[PossibleValue] = {
    val peList = (for (season <- SeasonInfo(calculateStartYear, Season.Q1) until calcSeason) yield peOf(season)).filter(_ > 0)
    Set(PossibleValue(peList.max, 0.40), PossibleValue(peList.min, 0.40), PossibleValue(peList.sum / peList.length, 0.80))
  }

  // 通过估计值，计算股票可能的价格
  def calcPossiblePrice(startSeason: SeasonInfo, targetSeason: SeasonInfo, possibleNetProfit: Long, possiblePe: Double): Double = {
    // 开始的计算的季度可能过早，改公司还没有上市，所以可能需要判断是否需要往后延
    var startPrice: Double = findEarliestAvailablePrice(startSeason)
    // 计算出目前季度的上个记录的可能价格
    for (season <- startSeason until targetSeason) {
      try {
        startPrice += ((netProfitByShareOf(season) - netProfitByShareOf(season.lastYear)) * peOf(season))
      } catch {
        case e: Exception => log.info(s"can not net profit of season: $season. skip.", e)
      }
    }
    (startPrice + (possibleNetProfit - netProfitByShareOf(targetSeason.lastYear)) * possiblePe) / totalShares
  }

  private def findEarliestAvailablePrice(fromSeason: SeasonInfo): Double = {
    var findSeason = fromSeason
    while (findSeason <= price.maxSeason && price(findSeason).isEmpty) {
      findSeason = findSeason.nextSeason
    }
    try {
      price(findSeason).get.calcPrice
    } catch {
      case e: Exception =>
        throw new IllegalArgumentException(s"price info not enough to find expected season: $findSeason", e)
    }
  }
}

object Stock extends App {
  def calcPe(capitalization: Double, netProfit: Double): Double = capitalization / netProfit
}

case class StockCode(code: String) {
  val fullCode = if (code.startsWith("0") || code.startsWith("300")) s"sz$code"
  else if (code.startsWith("6")) s"sh$code"
  else throw new IllegalArgumentException(s"unknown stock code $code")
}
