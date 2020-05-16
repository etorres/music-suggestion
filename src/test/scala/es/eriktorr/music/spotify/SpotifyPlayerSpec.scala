package es.eriktorr.music.spotify

import com.github.tomakehurst.wiremock.client.WireMock._
import es.eriktorr.music.unitspec.HttpServerSpec

class SpotifyPlayerSpec extends HttpServerSpec {
  "Spotify player" should "get current's user recently played tracks" in {
    val path = "/v1/me/player/recently-played"
    val token = "Nej3WCRXQs0_"

    stubFor(
      get(path)
        .withHeader("Authorization", equalTo(s"Bearer $token"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withStatusMessage("OK")
            .withHeader("Content-Type", "application/json")
            .withBody(resourceAsString("spotify/recently_played_tracks.json"))
        )
    )

    val spotifyPlayer = new SpotifyPlayerBackend
    val tracks = spotifyPlayer.recentlyPlayedTracks(
      authorizationBearer = token,
      playerEndpoint = spotifyConfig().endpoints.recentlyPlayed
    )

    verifyGetRequestTo(path)

    tracks.getOrElse(InvalidTracks).items should have size 20
  }

  private[this] lazy val InvalidTracks = SpotifyTracks(
    items = Seq.empty,
    next = "",
    cursors = SpotifyCursor(
      after = "",
      before = ""
    ),
    limit = 0,
    href = ""
  )
}
