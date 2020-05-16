package es.eriktorr.music.recommendation

import es.eriktorr.music.unitspec.{HttpServerSpec, LambdaRuntimeStubs}

class MusicRecommenderSpec extends HttpServerSpec with LambdaRuntimeStubs {
//  private[this] val recommender = new MusicRecommender(
//    ApplicationContext(spotifyConfig(), usersConfig())
//  )

  "Recommendation proxy" should "create a playlist with tracks matched against input parameters" in {
//    recommender.handle()
//    recommender.handleRequest(
//      aRequest(Some("""{"energetic":true}"""), Map("playlistName" -> "My Playlist")),
//      ContextStub
//    )
    // TODO
  }

  it should "create a playlist using default parameters" in {
//    recommender.handleRequest(aRequest(None, Map.empty), ContextStub)
    // TODO
  }

  it should "return an empty body on an error" in {
//    recommender
//      .handleRequest(aRequest(None, Map.empty), ContextStub)
//      .getBody shouldBe """{"playlists":[]"""
    // TODO
  }
}
