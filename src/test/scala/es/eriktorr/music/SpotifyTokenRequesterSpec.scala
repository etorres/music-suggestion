package es.eriktorr.music

import es.eriktorr.music.unitspec.HttpServerSpec
import com.github.tomakehurst.wiremock.client.WireMock._
import com.typesafe.config.ConfigFactory

class SpotifyTokenRequesterSpec extends HttpServerSpec {
  "Spotify token request" should "exchange client credentials for an access token" in {
    stubFor(
      post("/api/token")
        .withHeader("Authorization", equalTo("Basic Y2xpZW50LWlkOmNsaWVudC1zZWNyZXQ="))
        .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
        .withRequestBody(equalTo("grant_type=client_credentials"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withStatusMessage("OK")
            .withHeader("Content-Type", "application/json")
            .withBody(s"""
                         |{
                         |  "access_token": "$AccessToken",
                         |  "token_type": "Bearer",
                         |  "expires_in": 3600,
                         |  "scope": ""
                         |}
                         |""".stripMargin)
        )
    )

    val spotifyConfig = ApplicationContextLoader
      .applicationContextFrom(
        ConfigFactory.parseResources("application-test.conf")
      )
      .spotifyConfig

    val spotifyTokenRequester = new SpotifyTokenRequester
    val token =
      spotifyTokenRequester.token(spotifyConfig.authorizationEndpoint, spotifyConfig.credentials)

    token.getOrElse(InvalidToken) shouldBe SpotifyToken(
      access_token = AccessToken,
      token_type = "Bearer",
      expires_in = 3600L
    )
  }

  private[this] lazy val AccessToken =
    "BQD2nqw3byf_BtPgD14X7Q97AdXhrxm3Kkq_wTscvq2_IJUiI7qEPIRVpLNF7k2EWzOyvaQ4LuZdVkkWBcE"

  private[this] lazy val InvalidToken = SpotifyToken("", "", -1L)
}
