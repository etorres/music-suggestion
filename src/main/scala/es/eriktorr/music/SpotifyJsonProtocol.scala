package es.eriktorr.music

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
}

object SpotifyJsonProtocol extends SpotifyJsonProtocol
