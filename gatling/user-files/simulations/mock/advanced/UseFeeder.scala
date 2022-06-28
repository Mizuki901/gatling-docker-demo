package mock.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import util.HttpClient

class UseFeeder extends Simulation {

  // テスト対象のURL
  val baseUrl = "http://host.docker.internal:5000" // host.docker.internalは、Docker for Mac/Windows上のコンテナからホストOSを参照するためのドメイン
  val httpProtocol = HttpClient.createProtocol(baseUrl)

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
  setUp(scn.inject(constantUsersPerSec(5).during(24.hours)).protocols(httpProtocol))
}
