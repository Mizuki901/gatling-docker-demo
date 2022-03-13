package mock.users

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GetUserSimuration extends Simulation {

  val httpProtocol = http
    // テスト対象のURL
    .baseUrl("http://host.docker.internal:5000") // host.docker.internalは、Docker for Mac/Windows上のコンテナからホストOSを参照するためのドメイン
    // リクエストヘッダ
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("ja,en-US;q=0.7,en;q=0.3")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  // テストシナリオ
  val scn = scenario("Get user by username")
    .exec(
      http("get_user")
        .get("/users")
        .queryParam("username", "Bret")
        .check(status.is(200))
        .check(jsonPath("$..id").is("1"))
        .check(jsonPath("$..email").is("Sincere@april.biz"))
        .check(jsonPath("$..company.name").is("Romaguera-Crona"))
    )

  // 負荷のかけ方
  setUp(scn.inject(constantUsersPerSec(20).during(30)).protocols(httpProtocol)) // Open Workload Model
  // setUp(scn.inject(constantConcurrentUsers(20).during(30)).protocols(httpProtocol)) // Closed Workload Model
}
