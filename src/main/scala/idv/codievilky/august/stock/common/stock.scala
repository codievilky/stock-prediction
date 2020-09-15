package idv.codievilky.august

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, PropertyNamingStrategy}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import idv.codievilky.august.stock.info.SeasonInfo

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
    val Q1, Q2, Q3, Q4 = Value

    def main(args: Array[String]): Unit = {
      val se = SeasonInfo(2013, Season.Q1);
      val se2 = SeasonInfo(2013, Season.Q1);
      println(se >= se2)
    }
  }

  type Season = Season.Value

  // 保留小数
  def keepNumber(value: Double, number: Int) = {
    val multiPle = Math.pow(10, number)
    (value * multiPle).toLong.toDouble / multiPle
  }
}
