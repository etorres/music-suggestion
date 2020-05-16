package es.eriktorr.music.unitspec

import es.eriktorr.music.spotify.{
  SpotifyAlbum,
  SpotifyArtist,
  SpotifyFollowers,
  SpotifyImage,
  SpotifyPlaylist,
  SpotifyPlaylistTracks,
  SpotifyTrack,
  SpotifyTrackItem,
  SpotifyUser
}

trait SpotifyWebApiStubs {
  val SpotifyImage1: SpotifyImage = SpotifyImage(height = 100, width = 100, url = "spotify:image1")

  val SpotifyArtist1: SpotifyArtist = SpotifyArtist(
    external_urls = Map.empty,
    href = "http://localhost/artist1",
    id = "artist1",
    name = "Artist 1",
    `type` = "artist",
    uri = "spotify:artist1"
  )

  val SpotifyAlbum1: SpotifyAlbum = SpotifyAlbum(
    album_type = "album",
    artists = Seq(SpotifyArtist1),
    available_markets = Seq("ES"),
    external_urls = Map.empty,
    href = "http://localhost/album1",
    id = "album1",
    images = Seq(SpotifyImage1),
    name = "Album 1",
    release_date = "1234567890",
    release_date_precision = "1",
    total_tracks = 10,
    `type` = "album",
    uri = "spotify:album1"
  )

  val SpotifyTrack1: SpotifyTrack = SpotifyTrack(
    album = SpotifyAlbum1,
    artists = Seq(SpotifyArtist1),
    available_markets = Seq("ES"),
    disc_number = 1,
    duration_ms = 1600,
    explicit = false,
    external_urls = Map.empty,
    href = "http://localhost/track1",
    id = "track1",
    is_local = false,
    name = "Track 1",
    popularity = 1,
    preview_url = None,
    track_number = 1,
    `type` = "track",
    uri = "spotify:track1"
  )

  val SpotifyUser1: SpotifyUser = SpotifyUser(
    display_name = "User 1",
    external_urls = Map.empty,
    href = "http://localhost/user1",
    id = "user1",
    `type` = "user",
    uri = "spotify:user1"
  )

  val SpotifyPlaylist1: SpotifyPlaylist = SpotifyPlaylist(
    collaborative = false,
    description = None,
    external_urls = Map("spotify" -> "https://lcoalhost/playlist/1"),
    followers = SpotifyFollowers(href = None, total = 1000),
    href = "http://localhost/playlist1",
    id = "playlist1",
    images = Seq(SpotifyImage1),
    name = "Playlist 1",
    owner = SpotifyUser1,
    primary_color = None,
    public = true,
    snapshot_id = "123",
    tracks = SpotifyPlaylistTracks(
      href = "http://localhost/playlist1/tracks",
      items = Seq(
        SpotifyTrackItem(
          track = SpotifyTrack1,
          played_at = "123",
          context = None
        )
      ),
      limit = 1,
      next = None,
      offset = 0,
      previous = None,
      total = 1
    ),
    `type` = "playlist",
    uri = "spotify:playlist1"
  )
}
