package util

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder

object HttpClient {
  def createProtocol (baseUrl: String): HttpProtocolBuilder = {
    http
      .baseUrl(baseUrl)
      // リクエストヘッダ
      .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
      .doNotTrackHeader("1")
      .acceptLanguageHeader("ja,en-US;q=0.7,en;q=0.3")
      .acceptEncodingHeader("gzip, deflate")
      .userAgentHeader("Gatling")
  }
}