package es.eriktorr.music

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, equalTo, get}
import es.eriktorr.music.unitspec.HttpServerSpec

class SpotifyRecommendationsSpec extends HttpServerSpec {
  "Spotify recommendations" should "get tracks matched against similar tracks" in {
    stubFor(
      get("/v1/recommendations?seed_tracks=65YTkL1HqiFPcAuCabVwMf,2KfWw8uwvCFOasBZ7xsmZo&limit=10")
        .withHeader("Authorization", equalTo("Bearer JqyrxCsALiot"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withStatusMessage("OK")
            .withHeader("Content-Type", "application/json")
            .withBody(resourceAsString("spotify/recommendations.json"))
        )
    )

    val spotifyRecommender = new SpotifyRecommender
    val recommendations = spotifyRecommender.recommendedTracks(
      authorizationBearer = "JqyrxCsALiot",
      recommendationsEndpoint = spotifyConfig().endpoints.recommendations,
      seedTracks = Seq("65YTkL1HqiFPcAuCabVwMf", "2KfWw8uwvCFOasBZ7xsmZo")
    )

    recommendations.getOrElse(InvalidRecommendations).tracks should have size 10
  }

  it should "fail with exception when no seed tracks are provided" in {
    val spotifyRecommender = new SpotifyRecommender
    the[IllegalArgumentException] thrownBy spotifyRecommender.recommendedTracks(
      authorizationBearer = "JqyrxCsALiot",
      recommendationsEndpoint = spotifyConfig().endpoints.recommendations,
      seedTracks = Seq()
    ) should have message "requirement failed: Up to 5 seed tracks may be provided"
  }

  it should "fail with exception when seed tracks limit is exceeded" in {
    val spotifyRecommender = new SpotifyRecommender
    the[IllegalArgumentException] thrownBy spotifyRecommender.recommendedTracks(
      authorizationBearer = "JqyrxCsALiot",
      recommendationsEndpoint = spotifyConfig().endpoints.recommendations,
      seedTracks = Seq("1", "2", "3", "4", "5", "6")
    ) should have message "requirement failed: Up to 5 seed tracks may be provided"
  }

  private[this] lazy val InvalidRecommendations =
    SpotifyRecommendations(tracks = Seq.empty, seeds = Seq.empty)
}
