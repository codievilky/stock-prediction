package idv.codievilky.august

import java.util.concurrent.TimeUnit

import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala.DefaultScalaModule
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
