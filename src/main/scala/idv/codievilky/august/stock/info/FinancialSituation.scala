package idv.codievilky.august.stock
package info

import idv.codievilky.august.stock.analyse.PossibleValue

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class FinancialSituation {
  val allFinancialInfo = new mutable.HashMap[SeasonInfo, FinancialInfo]()

  def +=(seasonFinancialInfo: (SeasonInfo, FinancialInfo)): Unit = {
    allFinancialInfo += seasonFinancialInfo
  }

  def apply(seasonInfo: SeasonInfo): FinancialInfo = allFinancialInfo(seasonInfo)

  def netProfitYearOnYearGrowthRatio(startSeason: SeasonInfo) = {
    (netProfit(startSeason) * 1000 / netProfit(startSeason.lastYear)).toDouble / 1000
  }

  def netProfit(season: SeasonInfo) = {
    val profit = if (season.season == Season.Q1) allFinancialInfo(season).totalNetProfit
    else allFinancialInfo(season).totalNetProfit - allFinancialInfo(season.lastSeason).totalNetProfit
    (profit * 1_0000_0000).toLong
  }

  /**
   * a1 b1 c1 d1
   * a2 b2 c2 d2
   *
   * 求 c2 = c1 * (a2 / a1)
   */
  private[info] def guessBySeasonIncrease(expectedSeason: SeasonInfo): List[PossibleValue] = {
    if (expectedSeason.season == Season.Q1) {
      return List()
    }
    val lastYearProfit = netProfit(expectedSeason.lastYear)
    val expectedProfit = new ListBuffer[Long]
    for (seasonId <- 1 until expectedSeason.season.id) {
      val seasonInfo = SeasonInfo(expectedSeason.year, Season(seasonId))
      expectedProfit += (lastYearProfit * netProfitYearOnYearGrowthRatio(seasonInfo)).toLong
    }
    for (profit <- expectedProfit.toList) yield PossibleValue(profit, 0.65)
  }

  /**
   * 环比数据估算
   */
  private[info] def guessByYearIncrease(expectedSeason: SeasonInfo, calcStartYear: Int): PossibleValue = {
    var startSeason = SeasonInfo(calcStartYear, expectedSeason.season)
    var increaseSum = 0D
    var calcYearNum = 0
    while (startSeason < expectedSeason) {
      increaseSum += netProfitYearOnYearGrowthRatio(startSeason)
      startSeason += 4
      calcYearNum += 1
    }
    val lastYearProfit = netProfit(expectedSeason.lastYear)
    PossibleValue((lastYearProfit * increaseSum / calcYearNum).toLong, 0.70)
  }
}


