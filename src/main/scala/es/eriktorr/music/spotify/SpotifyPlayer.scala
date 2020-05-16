package es.eriktorr.music.spotify

import es.eriktorr.music.spotify.SpotifyJsonProtocol._
import sttp.client._

trait SpotifyPlayer {
  def recentlyPlayedTracks(
    authorizationBearer: String,
    playerEndpoint: String
  ): Either[String, SpotifyTracks]
}

final class SpotifyPlayerBackend extends SpotifyPlayer with SpotifyBackend {
  def recentlyPlayedTracks(
    authorizationBearer: String,
    playerEndpoint: String
  ): Either[String, SpotifyTracks] = {
    val request = basicRequest
      .header("Authorization", s"Bearer $authorizationBearer")
      .get(uri"$playerEndpoint")

    val response = send(request)
    decodeJson[SpotifyTracks](
      response,
      errorMessage = "Cannot get current's user recently played tracks"
    )
  }
}
