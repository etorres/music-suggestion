package es.eriktorr.music.spotify

import com.github.tomakehurst.wiremock.client.WireMock._
import es.eriktorr.music.UsersConfig
import es.eriktorr.music.unitspec.HttpServerSpec

class SpotifyTokenRequesterSpec extends HttpServerSpec {
  "Spotify token request" should "exchange client credentials for an access token" in {
    val path = "/api/token"
    val accessToken =
      "BQD2nqw3byf_BtPgD14X7Q97AdXhrxm3Kkq_wTscvq2_IJUiI7qEPIRVpLNF7k2EWzOyvaQ4LuZdVkkWBcE"
    val currentSpotifyConfig = spotifyConfig()
    val currentUser = currentUserFrom(usersConfig())

    stubFor(
      post(path)
        .withHeader("Authorization", equalTo("Basic Y2xpZW50LWlkOmNsaWVudC1zZWNyZXQ="))
        .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
        .withRequestBody(
          equalTo(s"grant_type=refresh_token&refresh_token=${currentUser.refreshToken}")
        )
        .willReturn(
          aResponse()
            .withStatus(200)
            .withStatusMessage("OK")
            .withHeader("Content-Type", "application/json")
            .withBody(s"""
                         |{
                         |  "access_token": "$accessToken",
                         |  "token_type": "Bearer",
                         |  "expires_in": 3600,
                         |  "scope": "playlist-read-private"
                         |}
                         |""".stripMargin)
        )
    )

    val spotifyTokenRequester = new SpotifyTokenRequester
    val token =
      spotifyTokenRequester.token(
        currentSpotifyConfig.endpoints.authorization,
        currentSpotifyConfig.credentials,
        currentUser.refreshToken
      )

    verifyPostRequestTo(path)

    token.getOrElse(InvalidToken) shouldBe SpotifyToken(
      access_token = accessToken,
      token_type = "Bearer",
      scope = "playlist-read-private",
      expires_in = 3600L
    )
  }

  // Needed to get a user from the configuration
  @SuppressWarnings(Array("org.wartremover.warts.OptionPartial"))
  private[this] def currentUserFrom(usersConfig: UsersConfig) =
    usersConfig.users.find(_.userId == "the_lin_michael").get

  private[this] lazy val InvalidToken = SpotifyToken("", "", "", -1L)
}
