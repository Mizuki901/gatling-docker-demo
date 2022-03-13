package mock

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class GetUserAndPostsSimuration extends Simulation {

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
  val scn = scenario("Get posts by userid")
    // /users: usernameでuseridを取得
    .exec(
      http("get_user")
        .get("/users")
        .queryParam("username", "Bret")
        .check(status.is(200))
        .check(jsonPath("$..id").saveAs("userid"))
    )
    // /posts: 先ほど取得したuseridを使って、postsを取得
    .exec(
      http("get_posts")
        .get("/posts")
        .queryParam("userid", "${userid}")
        .check(status.is(200))
    )

  // 負荷のかけ方
  setUp(scn.inject(constantUsersPerSec(20).during(30)).protocols(httpProtocol)) // Open Workload Model
}
