package es.eriktorr.music.recommendation

import es.eriktorr.music.spotify._
import es.eriktorr.music.unitspec.{HttpServerSpec, LambdaRuntimeStubs, SpotifyWebApiStubs}
import es.eriktorr.music.{ApplicationContext, SpotifyCredentials}

class MusicRecommenderSpec extends HttpServerSpec with LambdaRuntimeStubs with SpotifyWebApiStubs {
  import MusicRecommenderJsonProtocol._

  "Music recommender" should "create a playlist with tracks matched against input parameters" in {
    aMusicRecommender.handle(
      parameters = Map("playlistName" -> "My Playlist"),
      musicFeatures =
        MusicFeatures(None, None, energetic = Some(true), None, None, None, None, None),
      awsLambdaContext = ContextStub
    ) shouldBe MusicRecommendation(playlists =
      Seq(MusicPlaylist(name = "My Playlist", service = "spotify", url = "spotify:my_playlist"))
    )
  }

  it should "create a playlist using default parameters" in {
    aMusicRecommender.handle(
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
    aMusicRecommender.handle(
      parameters = Map.empty,
      musicFeatures = MusicFeatures(None, None, None, None, None, None, None, None),
      awsLambdaContext = ContextStub
    ) shouldBe MusicRecommendation(playlists = Seq.empty)
  }

  private[this] def aMusicRecommender =
    new MusicRecommender(
      applicationContext = ApplicationContext(spotifyConfig(), usersConfig()),
      spotifyTokenRequester = new SpotifyTokenRequesterFake,
      spotifyPlayer = new SpotifyPlayerFake,
      spotifyRecommender = new SpotifyRecommenderFake,
      spotifyPlaylistModifier = new SpotifyPlaylistModifierFake
    )

  final class SpotifyTokenRequesterFake extends SpotifyTokenRequester {
    override def token(
      authorizationEndpoint: String,
      credentials: SpotifyCredentials,
      refreshToken: String
    ): Either[String, SpotifyToken] =
      Right(
        SpotifyToken(
          access_token = "IJUiI7qEPIRVpLNF7k2EWzOyvaQ4LuZdVkkWBcE",
          token_type = "Bearer",
          scope = "playlist-read-private",
          expires_in = 3600L
        )
      )
  }

  final class SpotifyPlayerFake extends SpotifyPlayer {
    override def recentlyPlayedTracks(
      authorizationBearer: String,
      playerEndpoint: String
    ): Either[String, SpotifyTracks] =
      Right(
        SpotifyTracks(
          items = Seq(
            SpotifyTrackItem(
              track = SpotifyTrack1,
              played_at = "123",
              context = None
            )
          ),
          next = "track2",
          cursors = SpotifyCursor(after = "2", before = "4"),
          limit = -1,
          href = "tracks"
        )
      )
  }

  final class SpotifyRecommenderFake extends SpotifyRecommender {
    override def recommendedTracks(
      authorizationBearer: String,
      recommendationsEndpoint: String,
      seedTracks: Seq[String],
      musicFeatures: MusicFeatures
    ): Either[String, SpotifyRecommendations] =
      Right(
        SpotifyRecommendations(
          tracks = Seq(SpotifyTrack1),
          seeds = Seq(
            SpotifySeed(
              initialPoolSize = 10,
              afterFilteringSize = 10,
              afterRelinkingSize = 10,
              id = "seed1",
              `type` = "seed",
              href = "seed1"
            )
          )
        )
      )
  }

  final class SpotifyPlaylistModifierFake extends SpotifyPlaylistModifier {
    override def create(
      name: String,
      userId: String,
      authorizationBearer: String,
      createPlaylistEndpoint: String
    ): Either[String, SpotifyPlaylist] = Right(SpotifyPlaylist1)

    override def addItemsTo(
      playlistId: String,
      items: SpotifyUris,
      authorizationBearer: String,
      addItemsEndpoint: String
    ): Either[String, SpotifySnapshotId] = Right(SpotifySnapshotId(snapshot_id = "123"))
  }
}
