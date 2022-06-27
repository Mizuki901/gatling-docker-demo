package mock.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class UseFeeder extends Simulation {

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
        .get("${path}") // ${...} : EL(Expression Language)式でcsvデータを動的に扱える
        .check(status.is("${expected_status_code}"))
        .check(jsonPath("$..name").is("${expected_name}"))
    )

  // シナリオを実行する
  setUp(scn.inject(constantUsersPerSec(1).during(24.hours)).protocols(httpProtocol))
}
