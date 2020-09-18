package idv.codievilky.august.stock
package info

import idv.codievilky.august.common.Season.Season
import idv.codievilky.august.stock.analyse.PossibleValue

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class FinancialSituation {
  def this(financialInfoIter: Iterator[FinancialInfo]) = {
    this()
    financialInfoIter.foldLeft(allFinancialInfo) { (m, f) =>
      m += (f.seasonInfo -> f)
    }
  }

  val allFinancialInfo = new mutable.HashMap[SeasonInfo, FinancialInfo]()

  def +=(seasonFinancialInfo: (SeasonInfo, FinancialInfo)): Unit = {
    allFinancialInfo += seasonFinancialInfo
  }

  def apply(seasonInfo: SeasonInfo): FinancialInfo = {
    allFinancialInfo.get(seasonInfo) match {
      case Some(info) => info
      case None => throw new IllegalArgumentException(s"can not find financial info at season: $seasonInfo")
    }
  }

  def update(seasonInfo: SeasonInfo, financialInfo: FinancialInfo): Unit = allFinancialInfo.update(seasonInfo, financialInfo);

  def netProfitYearOnYearGrowthRatio(startSeason: SeasonInfo) = {
    netProfit(startSeason).toDouble / netProfit(startSeason.lastYear)
  }

  def netProfit(season: SeasonInfo) = {
    if (season.season == Season.Q1) this (season).totalNetProfit
    else this (season).totalNetProfit - this (season.lastSeason).totalNetProfit
  }

  /**
   * a1 b1 c1 d1
   * a2 b2 c2 d2
   *
   * 求 c2 = c1 * (a2 / a1)
   */
  private[info] def guessBySeasonIncrease(expectedSeason: SeasonInfo): Set[PossibleValue] = {
    if (expectedSeason.season == Season.Q1) {
      return Set()
    }
    val lastYearProfit = netProfit(expectedSeason.lastYear)
    val expectedProfit = new mutable.HashSet[Long]
    for (seasonId <- 1 until expectedSeason.season.id) {
      val seasonInfo = SeasonInfo(expectedSeason.year, Season.of(seasonId))
      expectedProfit += (lastYearProfit * netProfitYearOnYearGrowthRatio(seasonInfo)).toLong
    }
    (for (profit <- expectedProfit) yield PossibleValue(profit, 0.65)).toSet
  }

  /**
   * 环比数据估算
   */
  private[info] def guessByYearIncrease(expectedSeason: SeasonInfo, calcStartYear: Int): Set[PossibleValue] = {
    var startSeason = List(allFinancialInfo.keySet.min.nextYear + 1, SeasonInfo(calcStartYear, expectedSeason.season)).max
    var increaseSum = 0D
    var calcYearNum = 0
    while (startSeason < expectedSeason) {
      increaseSum += netProfitYearOnYearGrowthRatio(startSeason)
      startSeason += 4
      calcYearNum += 1
    }
    if (calcYearNum == 0) {
      return Set()
    }
    val lastYearProfit = netProfit(expectedSeason.lastYear)
    Set(PossibleValue((lastYearProfit * increaseSum / calcYearNum).toLong, 0.70))
  }
}


