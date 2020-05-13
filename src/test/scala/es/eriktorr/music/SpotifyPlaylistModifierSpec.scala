package es.eriktorr.music

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, equalTo, post}
import es.eriktorr.music.unitspec.HttpServerSpec

class SpotifyPlaylistModifierSpec extends HttpServerSpec {
  "Spotify playlist modifier" should "create an empty public playlist" in {
    val path = "/v1/users/the_lin_michael/playlists"
    stubFor(
      post(path)
        .withHeader("Authorization", equalTo("Bearer pV5p1P98h8Dg"))
        .withHeader("Content-Type", equalTo("application/json"))
        .withRequestBody(
          equalTo(s"""{"name":"My playlist","public":true}""")
        )
        .willReturn(
          aResponse()
            .withStatus(201)
            .withStatusMessage("Created")
            .withHeader("Content-Type", "application/json")
            .withBody(resourceAsString("spotify/created_playlist.json"))
        )
    )

    val playlistModifier = new SpotifyPlaylistModifier
    val playlist = playlistModifier.create(
      name = "My playlist",
      userId = "the_lin_michael",
      authorizationBearer = "pV5p1P98h8Dg",
      createPlaylistEndpoint = spotifyConfig().endpoints.playlists.create
    )

    verifyPostRequestTo(path)

    playlist.getOrElse(InvalidPlaylist) shouldBe SpotifyPlaylist(
      collaborative = false,
      description = None,
      external_urls = Map("spotify" -> "https://open.spotify.com/playlist/6tca25W3oaAx24q1JQKFg0"),
      followers = SpotifyFollowers(href = None, total = 0),
      href = "https://api.spotify.com/v1/playlists/6tca25W3oaAx24q1JQKFg0",
      id = "6tca25W3oaAx24q1JQKFg0",
      images = Seq.empty,
      name = "My playlist",
      owner = SpotifyUser(
        display_name = "the_lin_michael",
        external_urls = Map("spotify" -> "https://open.spotify.com/user/the_lin_michael"),
        href = "https://api.spotify.com/v1/users/the_lin_michael",
        id = "the_lin_michael",
        `type` = "user",
        uri = "spotify:user:the_lin_michael"
      ),
      primary_color = None,
      public = true,
      snapshot_id = "MSwyZGE4NjlhN2M2ODZkZGFkYjM3MWVjMWY0NmJkMDk0Yzk3NzVkOWUy",
      tracks = SpotifyPlaylistTracks(
        href = "https://api.spotify.com/v1/playlists/6tca25W3oaAx24q1JQKFg0/tracks",
        items = Seq.empty,
        limit = 100,
        next = None,
        offset = 0,
        previous = None,
        total = 0
      ),
      `type` = "playlist",
      uri = "spotify:playlist:6tca25W3oaAx24q1JQKFg0"
    )
  }

  private[this] lazy val InvalidPlaylist = SpotifyPlaylist(
    collaborative = false,
    description = None,
    external_urls = Map.empty,
    followers = SpotifyFollowers(None, -1),
    href = "",
    id = "",
    images = Seq.empty,
    name = "",
    owner = SpotifyUser(
      display_name = "",
      external_urls = Map.empty,
      href = "",
      id = "",
      `type` = "",
      uri = ""
    ),
    primary_color = None,
    public = false,
    snapshot_id = "",
    tracks = SpotifyPlaylistTracks(
      href = "",
      items = Seq.empty,
      limit = -1,
      next = None,
      offset = -1,
      previous = None,
      total = -1
    ),
    `type` = "",
    uri = ""
  )
}
