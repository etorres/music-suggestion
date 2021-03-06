package es.eriktorr.music.spotify

import spray.json._

sealed trait SpotifyJson

sealed case class SpotifyToken(
  access_token: String,
  token_type: String,
  scope: String,
  expires_in: Long
) extends SpotifyJson

sealed case class SpotifyArtist(
  external_urls: Map[String, String],
  href: String,
  id: String,
  name: String,
  `type`: String,
  uri: String
) extends SpotifyJson

sealed case class SpotifyImage(
  height: Int,
  width: Int,
  url: String
) extends SpotifyJson

sealed case class SpotifyAlbum(
  album_type: String,
  artists: Seq[SpotifyArtist],
  available_markets: Seq[String],
  external_urls: Map[String, String],
  href: String,
  id: String,
  images: Seq[SpotifyImage],
  name: String,
  release_date: String,
  release_date_precision: String,
  total_tracks: Int,
  `type`: String,
  uri: String
) extends SpotifyJson

sealed case class SpotifyTrack(
  album: SpotifyAlbum,
  artists: Seq[SpotifyArtist],
  available_markets: Seq[String],
  disc_number: Int,
  duration_ms: Int,
  explicit: Boolean,
  external_urls: Map[String, String],
  href: String,
  id: String,
  is_local: Boolean,
  name: String,
  popularity: Int,
  preview_url: Option[String],
  track_number: Int,
  `type`: String,
  uri: String
) extends SpotifyJson

sealed case class SpotifyContext(
  external_urls: Map[String, String],
  href: String,
  `type`: String,
  uri: String
) extends SpotifyJson

sealed case class SpotifyTrackItem(
  track: SpotifyTrack,
  played_at: String,
  context: Option[SpotifyContext]
) extends SpotifyJson

sealed case class SpotifyCursor(
  after: String,
  before: String
) extends SpotifyJson

sealed case class SpotifyTracks(
  items: Seq[SpotifyTrackItem],
  next: String,
  cursors: SpotifyCursor,
  limit: Int,
  href: String
) extends SpotifyJson

sealed case class SpotifySeed(
  initialPoolSize: Int,
  afterFilteringSize: Int,
  afterRelinkingSize: Int,
  id: String,
  `type`: String,
  href: String
) extends SpotifyJson

sealed case class SpotifyRecommendations(tracks: Seq[SpotifyTrack], seeds: Seq[SpotifySeed])
    extends SpotifyJson

sealed case class SpotifyFollowers(href: Option[String], total: Int) extends SpotifyJson

sealed case class SpotifyUser(
  display_name: String,
  external_urls: Map[String, String],
  href: String,
  id: String,
  `type`: String,
  uri: String
) extends SpotifyJson

sealed case class SpotifyPlaylistTracks(
  href: String,
  items: Seq[SpotifyTrackItem],
  limit: Int,
  next: Option[SpotifyTrackItem],
  offset: Int,
  previous: Option[SpotifyTrackItem],
  total: Int
) extends SpotifyJson

sealed case class SpotifyPlaylist(
  collaborative: Boolean,
  description: Option[String],
  external_urls: Map[String, String],
  followers: SpotifyFollowers,
  href: String,
  id: String,
  images: Seq[SpotifyImage],
  name: String,
  owner: SpotifyUser,
  primary_color: Option[String],
  public: Boolean,
  snapshot_id: String,
  tracks: SpotifyPlaylistTracks,
  `type`: String,
  uri: String
) extends SpotifyJson

sealed case class SpotifyUris(uris: Seq[String]) extends SpotifyJson

sealed case class SpotifySnapshotId(snapshot_id: String) extends SpotifyJson

trait SpotifyJsonProtocol extends DefaultJsonProtocol {
  implicit def tokenFormat: RootJsonFormat[SpotifyToken] = jsonFormat4(SpotifyToken)
  implicit def imageFormat: RootJsonFormat[SpotifyImage] = jsonFormat3(SpotifyImage)
  implicit def albumFormat: RootJsonFormat[SpotifyAlbum] = jsonFormat13(SpotifyAlbum)
  implicit def artistFormat: RootJsonFormat[SpotifyArtist] = jsonFormat6(SpotifyArtist)
  implicit def trackFormat: RootJsonFormat[SpotifyTrack] = jsonFormat16(SpotifyTrack)
  implicit def contextFormat: RootJsonFormat[SpotifyContext] = jsonFormat4(SpotifyContext)
  implicit def trackItemFormat: RootJsonFormat[SpotifyTrackItem] = jsonFormat3(SpotifyTrackItem)
  implicit def cursorFormat: RootJsonFormat[SpotifyCursor] = jsonFormat2(SpotifyCursor)
  implicit def tracksFormat: RootJsonFormat[SpotifyTracks] = jsonFormat5(SpotifyTracks)
  implicit def seedFormat: RootJsonFormat[SpotifySeed] = jsonFormat6(SpotifySeed)
  implicit def recommendationsFormat: RootJsonFormat[SpotifyRecommendations] =
    jsonFormat2(SpotifyRecommendations)
  implicit def followersFormat: RootJsonFormat[SpotifyFollowers] = jsonFormat2(SpotifyFollowers)
  implicit def userFormat: RootJsonFormat[SpotifyUser] = jsonFormat6(SpotifyUser)
  implicit def playlistTracksFormat: RootJsonFormat[SpotifyPlaylistTracks] =
    jsonFormat7(SpotifyPlaylistTracks)
  implicit def playlistFormat: RootJsonFormat[SpotifyPlaylist] = jsonFormat15(SpotifyPlaylist)
  implicit def urisFormat: RootJsonFormat[SpotifyUris] = jsonFormat1(SpotifyUris)
  implicit def snapshotIdFormat: RootJsonFormat[SpotifySnapshotId] = jsonFormat1(SpotifySnapshotId)
}

object SpotifyJsonProtocol extends SpotifyJsonProtocol
