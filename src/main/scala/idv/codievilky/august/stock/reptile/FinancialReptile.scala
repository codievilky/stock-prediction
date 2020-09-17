package idv.codievilky.august.stock.reptile

import java.util.concurrent.TimeUnit

import grizzled.slf4j.Logger
import idv.codievilky.august.stock.info.{FinancialInfo, FinancialSituation, SeasonInfo}
import lombok.extern.slf4j.Slf4j
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import okhttp3.{OkHttpClient, Request}
import org.apache.commons.lang3.StringUtils

import scala.collection.mutable
import scala.util.Using

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
class FinancialReptile {
  private val httpClient: OkHttpClient = new OkHttpClient.Builder().callTimeout(20, TimeUnit.SECONDS).build()

  // 解析新浪的 url
  def querySinaFinancialSituation(stockCode: String) = {
    val url = s"http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/${stockCode}.phtml?qq-pf-to=pcqq.c2c"
    Using.resource(httpClient.newCall(new Request.Builder().url(url).build()).execute()) { res =>
      if (res.code() != 200) {
        throw new IllegalStateException(s"request failed. code: ${res.code()}, body: ${res.body().string()}")
      }
      val usedResponseIterator = (JsoupBrowser().parseString(res.body().string().replace("&nbsp;", "-")) >> element("#FundHoldSharesTable") >>
        elementList("td")).map(_.text).filter(StringUtils.isNotBlank(_)).grouped(2).grouped(11).
        map(l => l.foldLeft(mutable.HashMap[String, String]()) { (m, l) => m += (l.head -> l.last) })
      new FinancialSituation(usedResponseIterator.map(FinancialReptile.sinaFinancialDataParser(_)))
    }
  }
}

object FinancialReptile {
  // 解析新浪数据的
  def sinaFinancialDataParser(oneSeasonData: collection.Map[String, String]): FinancialInfo = {
    def convertStringPrice2Double(price: String): Double = {
      if (price == "-") 0D else price.init.filter(_ != ',').toDouble
    }

    var season: SeasonInfo = null
    var netProfit: Long = 0
    var revenue: Long = 0
    var cashFlowByShare: Double = 0
    for ((name, value) <- oneSeasonData) {
      name match {
        case "截止日期" => season = SeasonInfo.forDate(value)
        case "净利润" => netProfit = convertStringPrice2Double(value).toLong
        case "主营业务收入" => revenue = convertStringPrice2Double(value).toLong
        case "每股现金流" => cashFlowByShare = convertStringPrice2Double(value)
        case _ =>
      }
    }
    FinancialInfo(season, netProfit, revenue, cashFlowByShare)
  }
}

object RequestTest extends App {
  val log = Logger[this.type]()
  val reptile = new FinancialReptile
  val data = reptile.querySinaFinancialSituation("002241")
  println(data.allFinancialInfo)
}
