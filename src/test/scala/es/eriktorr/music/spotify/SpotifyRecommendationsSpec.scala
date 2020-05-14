package es.eriktorr.music.spotify

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, equalTo, get}
import es.eriktorr.music.unitspec.HttpServerSpec

class SpotifyRecommendationsSpec extends HttpServerSpec {
  "Spotify recommendations" should "get tracks matched against similar tracks" in {
    val track1 = "65YTkL1HqiFPcAuCabVwMf"
    val track2 = "2KfWw8uwvCFOasBZ7xsmZo"
    val path = s"/v1/recommendations?seed_tracks=$track1,$track2&limit=10"
    val token = "JqyrxCsALiot"

    stubFor(
      get(path)
        .withHeader("Authorization", equalTo(s"Bearer $token"))
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
      authorizationBearer = token,
      recommendationsEndpoint = spotifyConfig().endpoints.recommendations,
      seedTracks = Seq(track1, track2)
    )

    verifyGetRequestTo(path)

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
