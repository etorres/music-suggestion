package es.eriktorr.music

import es.eriktorr.music.SpotifyJsonProtocol._
import sttp.client._

class SpotifyRecommender extends SpotifyBackend {
  def recommendedTracks(
    authorizationBearer: String,
    recommendationsEndpoint: String,
    seedTracks: Seq[String]
  ): Either[String, SpotifyRecommendations] = {
    require(seedTracks.nonEmpty && seedTracks.size <= 5, "Up to 5 seed tracks may be provided")
    val queryParams =
      Map("seed_tracks" -> seedTracks.mkString(","), "limit" -> "10")
    val request = basicRequest
      .header("Authorization", s"Bearer $authorizationBearer")
      .get(uri"$recommendationsEndpoint?$queryParams")

    val response = send(request)
    decodeJson[SpotifyRecommendations](response)
  }
}
