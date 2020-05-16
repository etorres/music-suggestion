package es.eriktorr.music.spotify

import es.eriktorr.music.recommendation.MusicFeatures
import es.eriktorr.music.spotify.SpotifyJsonProtocol._
import sttp.client._

trait SpotifyRecommender {
  def recommendedTracks(
    authorizationBearer: String,
    recommendationsEndpoint: String,
    seedTracks: Seq[String],
    musicFeatures: MusicFeatures
  ): Either[String, SpotifyRecommendations]
}

final class SpotifyRecommenderBackend extends SpotifyRecommender with SpotifyBackend {
  def recommendedTracks(
    authorizationBearer: String,
    recommendationsEndpoint: String,
    seedTracks: Seq[String],
    musicFeatures: MusicFeatures
  ): Either[String, SpotifyRecommendations] = {
    require(seedTracks.nonEmpty && seedTracks.size <= 5, "Up to 5 seed tracks may be provided")
    val queryParams =
      Map("seed_tracks" -> seedTracks.mkString(","), "limit" -> "10") ++ queryParametersFrom(
        musicFeatures
      )
    val request = basicRequest
      .header("Authorization", s"Bearer $authorizationBearer")
      .get(uri"$recommendationsEndpoint?$queryParams")

    val response = send(request)
    decodeJson[SpotifyRecommendations](response)
  }

  private[this] def queryParametersFrom(musicFeatures: MusicFeatures): Map[String, String] = {
    def trackFeature(condition: Option[Boolean], attribute: String): Option[(String, String)] =
      condition match {
        case Some(a) => Some(if (a) s"min_$attribute" -> "0.66" else s"max_$attribute" -> "0.33")
        case None => None
      }
    Seq(
      trackFeature(musicFeatures.acoustic, "acousticness"),
      trackFeature(musicFeatures.danceable, "danceability"),
      trackFeature(musicFeatures.energetic, "energy"),
      trackFeature(musicFeatures.instrumental, "instrumentalness"),
      trackFeature(musicFeatures.live, "liveness"),
      trackFeature(musicFeatures.loud, "loudness"),
      trackFeature(musicFeatures.spoken, "speechiness"),
      trackFeature(musicFeatures.happy, "valence")
    ).collect { case Some(value) => value }.toMap
  }
}
