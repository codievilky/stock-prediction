package idv.codievilky.august.stock
package info

import java.text.SimpleDateFormat
import java.util.{Calendar, TimeZone}

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer
import com.fasterxml.jackson.databind.{DeserializationContext, JsonSerializer, KeyDeserializer, SerializerProvider}

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */

//@JsonSerialize(using = classOf[SeasonInfoSerializer])
@JsonDeserialize(keyUsing = classOf[SeasonInfoKeyDeserializer])
case class SeasonInfo(year: Int, season: Season) extends Ordered[SeasonInfo] {
  def until(targetSeason: SeasonInfo): Seq[SeasonInfo] = {
    for (i <- seasonNumber until targetSeason.seasonNumber) yield SeasonInfo.forSeasonNumber(i)
  }

  override def >=(seasonInfo: SeasonInfo): Boolean = {
    if (year == seasonInfo.year) season.id >= seasonInfo.season.id else year >= seasonInfo.year
  }

  override def <=(seasonInfo: SeasonInfo): Boolean = {
    if (year == seasonInfo.year) season.id <= seasonInfo.season.id else year <= seasonInfo.year
  }

  override def <(seasonInfo: SeasonInfo): Boolean = {
    if (year == seasonInfo.year) season.id < seasonInfo.season.id else year < seasonInfo.year
  }

  override def compare(that: SeasonInfo): Int = {
    if (year == that.year) season.compare(that.season) else year.compare(that.year)
  }

  def seasonNumber: Int = year * 4 + season.id

  def +(plusNum: Int): SeasonInfo = {
    val newSeasonNum = seasonNumber + plusNum
    SeasonInfo.forSeasonNumber(newSeasonNum)
  }

  def -(minusNum: Int): SeasonInfo = {
    this + (-minusNum)
  }

  def nextSeason: SeasonInfo = this + 1

  def lastSeason: SeasonInfo = this - 1

  def nextYear: SeasonInfo = this + 4

  def lastYear: SeasonInfo = this - 4

  def timestamps = SeasonInfo.SIMPLE_DATE_FORMATTER.parse(s"$year-${season.date}").getTime


  override def toString = s"$year-${season.toString}"

  def lastDayOfSeason: DayInfo = DayInfo(year, season.month, season.day)
}

object SeasonInfo {
  def forSeasonNumber(seasonNum: Int): SeasonInfo = {
    val year = seasonNum / 4
    if (seasonNum % 4 == 0) SeasonInfo(year - 1, Season.Q4)
    else SeasonInfo(year, Season.of(seasonNum % 4))
  }

  val SIMPLE_DATE_FORMATTER = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    format.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"))
    format
  }

  def chinaCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"))

  def forDate(date: String): SeasonInfo = {
    val calendar = chinaCalendar
    val requestDate = SIMPLE_DATE_FORMATTER.parse(date)
    calendar.setTime(requestDate)
    val year = calendar.get(Calendar.YEAR)
    Season.values.foreach { season =>
      val currentSeasonInfo = SeasonInfo(year, season.asInstanceOf[Season])
      if (currentSeasonInfo.timestamps >= requestDate.getTime) {
        return currentSeasonInfo
      }
    }
    throw new IllegalArgumentException(s"failed to find season. $date")
  }
}
/*
class SeasonInfoSerializer extends StringKeySerializer {

  override def serialize(value: Any, g: JsonGenerator, provider: SerializerProvider) = super.serialize(value, g, provider)

  def serilize(seasonInfo: SeasonInfo, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
    val value = seasonInfo.toString
    gen.writeStartObject()
    gen.writeString(value)
    gen.writeEndObject()
  }
}*/

class SeasonInfoKeyDeserializer extends KeyDeserializer {
  override def deserializeKey(keyValue: String, ctxt: DeserializationContext): SeasonInfo = {
    val listSplit = keyValue.split('-')
    SeasonInfo(Integer.parseInt(listSplit(0)), Season.of(listSplit(1)))
  }
}



