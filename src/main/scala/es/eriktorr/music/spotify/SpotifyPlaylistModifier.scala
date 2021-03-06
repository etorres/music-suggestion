package es.eriktorr.music.spotify

import es.eriktorr.music.spotify.SpotifyJsonProtocol._
import sttp.client._

trait SpotifyPlaylistModifier {
  def create(
    name: String,
    userId: String,
    authorizationBearer: String,
    createPlaylistEndpoint: String
  ): Either[String, SpotifyPlaylist]

  def addItemsTo(
    playlistId: String,
    items: SpotifyUris,
    authorizationBearer: String,
    addItemsEndpoint: String
  ): Either[String, SpotifySnapshotId]
}

final class SpotifyPlaylistModifierBackend extends SpotifyPlaylistModifier with SpotifyBackend {
  def create(
    name: String,
    userId: String,
    authorizationBearer: String,
    createPlaylistEndpoint: String
  ): Either[String, SpotifyPlaylist] = {
    val uri = createPlaylistEndpoint.replaceAll("\\{user_id}", userId)
    val request = basicRequest
      .header("Authorization", s"Bearer $authorizationBearer")
      .contentType("application/json")
      .body(s"""{"name":"$name","public":true}""")
      .post(uri"$uri")

    val response = send(request)
    decodeJson[SpotifyPlaylist](response, errorMessage = s"Cannot create the playlist $name")
  }

  def addItemsTo(
    playlistId: String,
    items: SpotifyUris,
    authorizationBearer: String,
    addItemsEndpoint: String
  ): Either[String, SpotifySnapshotId] = {
    val uri = addItemsEndpoint.replaceAll("\\{playlist_id}", playlistId)
    val request = basicRequest
      .header("Authorization", s"Bearer $authorizationBearer")
      .contentType("application/json")
      .body(asCompactJson(items))
      .post(uri"$uri")

    val response = send(request)
    decodeJson[SpotifySnapshotId](
      response,
      errorMessage = s"Cannot add items to the playlist $playlistId"
    )
  }

  protected def asCompactJson(spotifyUris: SpotifyUris): String = {
    import spray.json._
    spotifyUris.toJson.compactPrint
  }
}
