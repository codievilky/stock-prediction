package idv.codievilky.august.stock
package reptile

import grizzled.slf4j.Logger
import idv.codievilky.august.stock.info.{FinancialInfo, FinancialSituation, SeasonInfo}
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.apache.commons.lang3.StringUtils

import scala.collection.mutable

/**
 * @auther Codievilky August
 * @since 2020/9/5
 */
object SinaFinancialReptile {

  def querySinaFinancialSituation(stockCode: String) = new FinancialSituation(queryFinancialInfoIterator(stockCode))

  // 解析新浪的 url
  private def queryFinancialInfoIterator(stockCode: String) = {
    val url = s"http://vip.stock.finance.sina.com.cn/corp/go.php/vFD_FinanceSummary/stockid/${stockCode}.phtml?qq-pf-to=pcqq.c2c"
    val response = doGet(url)
    (JsoupBrowser().parseString(response.replace("&nbsp;", "-")) >>
      element("#FundHoldSharesTable") >>
      elementList("td")).map(_.text).filter(StringUtils.isNotBlank(_)).grouped(2).grouped(11)
      .map(l => l.foldLeft(mutable.HashMap[String, String]()) { (m, l) => m += (l.head -> l.last) })
      .map(sinaFinancialDataParser(_))
  }

  // 解析新浪数据的
  private def sinaFinancialDataParser(oneSeasonData: collection.Map[String, String]): FinancialInfo = {
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
  val data = SinaFinancialReptile.querySinaFinancialSituation("002241")
  println(data.allFinancialInfo)
}
