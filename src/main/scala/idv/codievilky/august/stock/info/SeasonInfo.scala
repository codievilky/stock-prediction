package idv.codievilky.august.stock
package info

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.annotation.{JsonDeserialize, JsonSerialize}
import com.fasterxml.jackson.databind.{DeserializationContext, JsonSerializer, KeyDeserializer, SerializerProvider}

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */

@JsonSerialize(using = classOf[SeasonInfoSerializer])
@JsonDeserialize(keyUsing = classOf[SeasonInfoKeyDeserializer])
case class SeasonInfo(year: Int, season: Season) extends Ordered[SeasonInfo] {
  def until(targetSeason: SeasonInfo): Seq[SeasonInfo] = {
    for (i <- seasonNumber until targetSeason.seasonNumber) yield SeasonInfo.getSeasonInfo(i)
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
    SeasonInfo.getSeasonInfo(newSeasonNum)
  }

  def -(minusNum: Int): SeasonInfo = {
    this + (-minusNum)
  }

  def nextSeason: SeasonInfo = this + 1

  def lastSeason: SeasonInfo = this - 1

  def nextYear: SeasonInfo = this + 4

  def lastYear: SeasonInfo = this - 4

  override def toString = s"$year-${season.toString}"
}

object SeasonInfo {
  def getSeasonInfo(seasonNum: Int): SeasonInfo = {
    val year = seasonNum / 4
    if (seasonNum % 4 == 0) SeasonInfo(year - 1, Season.Q4)
    else SeasonInfo(year, Season(seasonNum % 4))
  }
}

class SeasonInfoSerializer extends JsonSerializer[SeasonInfo] {
  override def serialize(seasonInfo: SeasonInfo, gen: JsonGenerator, serializers: SerializerProvider): Unit = {
    val value = seasonInfo.toString
    println(value)
    gen.writeStartObject()
    gen.writeString(value)
    gen.writeEndObject()
  }
}

class SeasonInfoKeyDeserializer extends KeyDeserializer {
  override def deserializeKey(keyValue: String, ctxt: DeserializationContext): SeasonInfo = {
    val listSplit = keyValue.split('-')
    SeasonInfo(Integer.parseInt(listSplit(0)), Season.withName(listSplit(1)))
  }
}



