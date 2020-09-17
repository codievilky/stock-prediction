package idv.codievilky.august.stock.info

import java.util.Calendar
import java.util.concurrent.TimeUnit

import idv.codievilky.august.stock.Season

/**
 * @auther Codievilky August
 * @since 2020/9/17
 */
case class DayInfo(year: Int, month: Int, day: Int) {
  def >=(compareDay: DayInfo) = toInt >= compareDay.toInt

  def toInt = year * 10000 + month * 100 + day * 100

  def +(nextNumDay: Int): DayInfo = {
    if (nextNumDay == 0) {
      return this
    }
    val newDate = SeasonInfo.chinaCalendar
    newDate.set(year, month, day)

    newDate.setTimeInMillis(newDate.getTimeInMillis + TimeUnit.DAYS.toMillis(nextNumDay))
    DayInfo(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH + 1), newDate.get(Calendar.DAY_OF_MONTH))
  }

  def -(beforeNumDay: Int): DayInfo = this + (-beforeNumDay)

  def lastDay: DayInfo = {
    this - 1
  }
}

object test {
  def main(args: Array[String]): Unit = {
    println(Season.Q1.date)
  }
}
