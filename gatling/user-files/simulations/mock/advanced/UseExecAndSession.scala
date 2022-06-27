package mock.advanced

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import util.HttpClient

class UseExecAndSession extends Simulation {

  // テスト対象のURL
  val baseUrl = "http://host.docker.internal:5000" // host.docker.internalは、Docker for Mac/Windows上のコンテナからホストOSを参照するためのドメイン
  val httpProtocol = HttpClient.createProtocol(baseUrl)

  // csvからリクエストケースを読み込む
  val feeder = csv("sample-paths.csv").queue

  // テストシナリオ
  val scn = scenario("Get user")
    // csvデータを使用する
    .feed(feeder)
    // csvデータの加工
    .exec(session => {
      // /user/1 → /user?id=1 の形式にパスを修正する
      val originPath = session("path").as[String]
      val apiPath = originPath.replaceAll("^[^\\/]*\\/", "").replaceAll("\\/[^\\/]*$", "")
      val id = originPath.replaceAll("^[^\\/]*\\/[^\\/]*\\/", "")
      val editedPath = s"/${apiPath}?id=${id}"

      // 加工した値をsessionにセットする
      val newSession = session.set("editedPath", editedPath)
      newSession
    })
    // リクエストの実行と結果の検証
    .exec(
      http("get_user")
        .get("${editedPath}")
        .check(status.is("${expected_status_code}"))
        .check(jsonPath("$..name").is("${expected_name}"))
    )

  // シナリオを実行する
  setUp(scn.inject(constantUsersPerSec(1).during(24.hours)).protocols(httpProtocol))
}
