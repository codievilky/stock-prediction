package idv.codievilky.august

import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.ser.ContextualEnumerationSerializer
import idv.codievilky.august.stock.info.SeasonInfo
import okhttp3.{OkHttpClient, Request}

import scala.util.Using

/**
 * @auther Codievilky August
 * @since 2020/9/13
 */
package object stock {
  val DEFAULT_OBJECT_MAPPER = new ObjectMapper
  DEFAULT_OBJECT_MAPPER.registerModule(DefaultScalaModule)
  DEFAULT_OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  DEFAULT_OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
  val JSON_WRITER = DEFAULT_OBJECT_MAPPER.writerWithDefaultPrettyPrinter()


  object Season extends Enumeration(1) {
    def of(i: Int): Season = super.apply(i).asInstanceOf[SeasonValue]

    def of(name: String): Season = super.withName(name).asInstanceOf[SeasonValue]
    
    @JsonSerialize(using = classOf[SeasonSerializer])
    case class SeasonValue(month: Int, day: Int) extends Val {
      val date = f"$month%02d-$day%02d"
    }

    val Q1 = SeasonValue(3, 31)
    val Q2 = SeasonValue(6, 30)
    val Q3 = SeasonValue(9, 30)
    val Q4 = SeasonValue(12, 31)

    def main(args: Array[String]): Unit = {
      val se = SeasonInfo(2013, Season.Q1);
      val se2 = SeasonInfo(2013, Season.Q1);
      println(se >= se2)
    }
  }

  type Season = Season.SeasonValue


  private class SeasonSerializer extends JsonSerializer[scala.Enumeration#Value] with ContextualEnumerationSerializer {
    override def serialize(value: scala.Enumeration#Value, jgen: JsonGenerator, provider: SerializerProvider): Unit = {
      jgen.writeStartObject()
      jgen.writeStringField("enumClass", Season.SeasonValue.getClass.getName stripSuffix "$")
      jgen.writeStringField("value", value.toString)
      jgen.writeEndObject()
    }
  }

  // 保留小数
  def keepNumber(value: Double, number: Int) = {
    val multiPle = Math.pow(10, number)
    (value * multiPle).toLong.toDouble / multiPle
  }

  private val httpClient: OkHttpClient = new OkHttpClient.Builder().callTimeout(20, TimeUnit.SECONDS).build()

  def doGet(url: String): String = {
    Using.resource(httpClient.newCall(new Request.Builder().url(url).build()).execute()) { res =>
      val statusCode = res.code()
      if (statusCode == 404) {
        return null
      } else if (statusCode != 200) {
        throw new IllegalStateException(s"request failed. code: ${res.code()}, body: ${res.body().string()}")
      }
      res.body().string()
    }
  }
}
