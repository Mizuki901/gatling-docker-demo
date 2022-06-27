package mock.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import util._

class UseLogger extends Simulation {
  // 実行結果をcsv形式で独自に出力するためにLoggerを利用する
  val logger = LoggerFactory.getLogger(getClass.getName)
  // csvレポートにカラムを追加
  logger.info(
    CSVStringHelper.join(List("path", "expected_name", "actual_name", "expected_status_code", "actual_status_code"))
  )

  val httpProtocol = http
    // テスト対象のURL
    .baseUrl("http://host.docker.internal:5000") // host.docker.internalは、Docker for Mac/Windows上のコンテナからホストOSを参照するためのドメイン
    // リクエストヘッダ
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("ja,en-US;q=0.7,en;q=0.3")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // csvからリクエストケースを読み込む
  val feeder = csv("sample-paths.csv").queue

  // テストシナリオ
  val scn = scenario("Get user")
    // csvデータを使用する
    .feed(feeder)
    // リクエストの実行と結果の検証
    .exec(
      http("get_user")
        .get("${path}")
        .check(status.is("${expected_status_code}"))
        .check(status.saveAs("actual_status_code"))
        .check(jsonPath("$..name").is("${expected_name}"))
        .check(jsonPath("$..name").saveAs("actual_name"))
    )
    // 実行結果をcsvで出力する
    .exec(session => {
      val path = session("path").as[String]
      val expectedName = session("expected_name").as[String]
      val actualName = session("actual_name").as[String]
      val expectedStatusCode = session("expected_status_code").as[String]
      val actualStatusCode = session("actual_status_code").as[String]
      logger.info(
        CSVStringHelper.join(List(path, expectedName, actualName, expectedStatusCode, actualStatusCode))
      )
      session
    })

  // シナリオを実行する
  setUp(scn.inject(constantUsersPerSec(1).during(24.hours)).protocols(httpProtocol))
}
