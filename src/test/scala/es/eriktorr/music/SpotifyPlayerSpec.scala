package es.eriktorr.music

import com.github.tomakehurst.wiremock.client.WireMock._
import es.eriktorr.music.unitspec.HttpServerSpec

class SpotifyPlayerSpec extends HttpServerSpec {
  "Spotify player" should "get current's user recently played tracks" in {
    stubFor(
      get("/v1/me/player/recently-played")
        .withHeader("Authorization", equalTo("Bearer Nej3WCRXQs0_"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withStatusMessage("OK")
            .withHeader("Content-Type", "application/json")
            .withBody(resourceAsString("spotify/recently_played_tracks.json"))
        )
    )

    val spotifyPlayer = new SpotifyPlayer
    val tracks = spotifyPlayer.recentlyPlayedTracks(
      authorizationBearer = "Nej3WCRXQs0_",
      playerEndpoint = spotifyConfig().endpoints.recentlyPlayed
    )

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