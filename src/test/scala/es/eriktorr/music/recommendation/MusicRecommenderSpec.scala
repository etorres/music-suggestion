package es.eriktorr.music.recommendation

import es.eriktorr.music.ApplicationContext
import es.eriktorr.music.unitspec.{HttpServerSpec, LambdaRuntimeStubs}

class MusicRecommenderSpec extends HttpServerSpec with LambdaRuntimeStubs {
  import MusicRecommenderJsonProtocol._

  "Music recommender" should "create a playlist with tracks matched against input parameters" in {
    new MusicRecommender(
      ApplicationContext(spotifyConfig(), usersConfig())
    ).handle(
      parameters = Map("playlistName" -> "My Playlist"),
      musicFeatures =
        MusicFeatures(None, None, energetic = Some(true), None, None, None, None, None),
      awsLambdaContext = ContextStub
    ) shouldBe MusicRecommendation(playlists =
      Seq(MusicPlaylist(name = "My Playlist", service = "spotify", url = "spotify:my_playlist"))
    )
  }

  it should "create a playlist using default parameters" in {
    new MusicRecommender(
      ApplicationContext(spotifyConfig(), usersConfig())
    ).handle(
      parameters = Map.empty,
      musicFeatures = MusicFeatures(None, None, None, None, None, None, None, None),
      awsLambdaContext = ContextStub
    ) shouldBe MusicRecommendation(playlists =
      Seq(
        MusicPlaylist(
          name = "Indispensable Music",
          service = "spotify",
          url = "spotify:my_playlist"
        )
      )
    )
  }

  it should "return an empty body on an error" in {
    new MusicRecommender(
      ApplicationContext(spotifyConfig(), usersConfig())
    ).handle(
      parameters = Map.empty,
      musicFeatures = MusicFeatures(None, None, None, None, None, None, None, None),
      awsLambdaContext = ContextStub
    ) shouldBe MusicRecommendation(playlists = Seq.empty)
  }
}
