package idv.codievilky.august.stock.info

import idv.codievilky.august.stock.info.Season.Season

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */

case class SeasonInfo(year: Int, season: Season) extends Ordered[SeasonInfo] {
  override def >=(seasonInfo: SeasonInfo): Boolean = {
    if (year == seasonInfo.year) season.id >= seasonInfo.season.id else year >= seasonInfo.year
  }

  override def <=(seasonInfo: SeasonInfo): Boolean = {
    if (year == seasonInfo.year) season.id <= seasonInfo.season.id else year <= seasonInfo.year
  }

  override def compare(that: SeasonInfo): Int = {
    if (year == that.year) season.compare(that.season) else year.compare(that.year)
  }
}

object Season extends Enumeration {
  type Season = Value
  val Q1, Q2, Q3, Q4 = Value

  def main(args: Array[String]): Unit = {
    val se = SeasonInfo(2013, Q1);
    val se2 = SeasonInfo(2013, Q1);
    println(se >= se2)
  }
}



