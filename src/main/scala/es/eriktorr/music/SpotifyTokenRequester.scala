package es.eriktorr.music

import java.util.Base64

import es.eriktorr.music.SpotifyJsonProtocol._
import sttp.client._

class SpotifyTokenRequester extends SpotifyBackend {
  def token(
    authorizationEndpoint: String,
    credentials: SpotifyCredentials,
    refreshToken: String
  ): Either[String, SpotifyToken] = {
    val request = basicRequest
      .header("Authorization", basicAuthorizationFrom(credentials))
      .body(Map("grant_type" -> "refresh_token", "refresh_token" -> refreshToken))
      .post(uri"$authorizationEndpoint")

    val response = send(request)
    decodeJson[SpotifyToken](response)
  }

  private[this] def basicAuthorizationFrom(spotifyCredentials: SpotifyCredentials): String = {
    val encodedToken = Base64.getEncoder.encodeToString(
      s"${spotifyCredentials.clientId}:${spotifyCredentials.clientSecret}".getBytes
    )
    s"Basic $encodedToken"
  }
}
