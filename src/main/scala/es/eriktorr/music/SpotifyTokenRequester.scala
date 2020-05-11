package es.eriktorr.music

import java.util.Base64

import spray.json.{DefaultJsonProtocol, RootJsonFormat}
import sttp.client._

sealed case class SpotifyToken(access_token: String, token_type: String, expires_in: Long)

object SpotifyJsonProtocol extends DefaultJsonProtocol {
  implicit val tokenFormat: RootJsonFormat[SpotifyToken] = jsonFormat3(SpotifyToken)
}

class SpotifyTokenRequester {
  def token(
    authorizationEndpoint: String,
    spotifyCredentials: SpotifyCredentials
  ): Either[String, SpotifyToken] = {
    val request = basicRequest
      .header("Authorization", basicAuthorizationFrom(spotifyCredentials))
      .body(Map("grant_type" -> "client_credentials"))
      .post(uri"$authorizationEndpoint")
    val response = send(request)

    import SpotifyJsonProtocol._
    import spray.json._

    response.body.map(_.parseJson.convertTo[SpotifyToken])
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any"))
  private[this] def send(request: Request[Either[String, String], Nothing]) = {
    implicit val backend: SttpBackend[Identity, Nothing, NothingT] = HttpURLConnectionBackend()
    request.send()
  }

  private[this] def basicAuthorizationFrom(spotifyCredentials: SpotifyCredentials): String = {
    val encodedToken = Base64.getEncoder.encodeToString(
      s"${spotifyCredentials.clientId}:${spotifyCredentials.clientSecret}".getBytes
    )
    s"Basic $encodedToken"
  }
}
