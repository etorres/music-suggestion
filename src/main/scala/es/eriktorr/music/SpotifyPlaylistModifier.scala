package es.eriktorr.music

import es.eriktorr.music.SpotifyJsonProtocol._
import sttp.client._

class SpotifyPlaylistModifier extends SpotifyBackend {
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
    decodeJson[SpotifyPlaylist](response)
  }
}
