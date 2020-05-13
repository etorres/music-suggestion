package es.eriktorr.music

import es.eriktorr.music.SpotifyJsonProtocol._
import sttp.client._

class SpotifyPlayer extends SpotifyBackend {
  def recentlyPlayedTracks(
    authorizationBearer: String,
    playerEndpoint: String
  ): Either[String, SpotifyTracks] = {
    val request = basicRequest
      .header("Authorization", s"Bearer $authorizationBearer")
      .get(uri"$playerEndpoint")

    val response = send(request)
    decodeJson[SpotifyTracks](response)
  }
}