package es.eriktorr.music.recommendation

import com.amazonaws.services.lambda.runtime.Context
import es.eriktorr.music.aws.lambda.LambdaRequestHandler
import es.eriktorr.music.spotify._
import es.eriktorr.music.{ApplicationContextLoader, Logging, SpotifyConfig}
import spray.json.JsonFormat

final class MusicRecommender
    extends LambdaRequestHandler[MusicFeatures, MusicRecommendation]
    with Logging {

  private[this] val applicationContext = ApplicationContextLoader.applicationContext()

  override def handle(
    parameters: Map[String, String],
    musicFeatures: MusicFeatures,
    awsLambdaContext: Context
  )(
    implicit requestJsonFormat: JsonFormat[MusicFeatures],
    responseJsonFormat: JsonFormat[MusicRecommendation]
  ): MusicRecommendation = {
    logger.info(s"Recommending music with: ${musicFeatures.toString}, and: ${parameters.toString}")

    (for {
      userId <- userIdFrom(parameters)
      refreshToken <- refreshTokenFor(userId)
      spotifyConfig <- Right(applicationContext.spotifyConfig)
      token <- (new SpotifyTokenRequester).token(
        authorizationEndpoint = spotifyConfig.endpoints.authorization,
        credentials = spotifyConfig.credentials,
        refreshToken = refreshToken
      )
      seedTracks <- (new SpotifyPlayer)
        .recentlyPlayedTracks(
          authorizationBearer = token.access_token,
          playerEndpoint = spotifyConfig.endpoints.recentlyPlayed
        )
        .map(_.items.map(_.track.id).take(Defaults.MaximumSeedTracks))
      recommendedTracks <- (new SpotifyRecommender)
        .recommendedTracks(
          authorizationBearer = token.access_token,
          recommendationsEndpoint = spotifyConfig.endpoints.recommendations,
          seedTracks = seedTracks
        )
        .map(_.tracks.map(_.uri))
      playlist <- (new SpotifyPlaylistModifier).create(
        name = Defaults.PlaylistName,
        userId = userId,
        authorizationBearer = token.access_token,
        createPlaylistEndpoint = spotifyConfig.endpoints.playlists.create
      )
      playlistUrl <- playlistUrlFrom(playlist)
      musicRecommendation <- musicRecommendation(
        playlistId = playlist.id,
        playlistName = playlist.name,
        playlistUrl = playlistUrl,
        recommendedTracks = recommendedTracks,
        token = token,
        spotifyConfig = spotifyConfig
      )
    } yield musicRecommendation) match {
      case Right(musicRecommendation) => musicRecommendation
      case Left(errorMessage) =>
        logger.error(errorMessage)
        MusicRecommendation(Seq.empty)
    }
  }

  private[this] def refreshTokenFor(userId: String) =
    applicationContext.usersConfig.users.find(user => user.userId == userId) match {
      case Some(user) => Right(user.refreshToken)
      case None => Left(s"User not found: $userId")
    }

  private[this] def playlistUrlFrom(playlist: SpotifyPlaylist) =
    playlist.external_urls.get("spotify") match {
      case Some(url) => Right(url)
      case None => Left("Failed to create Spotify playlist")
    }

  private[this] def userIdFrom(parameters: Map[String, String]) =
    parameters.get("userId") match {
      case Some(value) => Right(value)
      case None => Left("Required parameter: userId")
    }

  private[this] def musicRecommendation(
    playlistId: String,
    playlistName: String,
    playlistUrl: String,
    recommendedTracks: Seq[String],
    token: SpotifyToken,
    spotifyConfig: SpotifyConfig
  ): Either[String, MusicRecommendation] =
    (new SpotifyPlaylistModifier).addItemsTo(
      playlistId = playlistId,
      items = SpotifyUris(recommendedTracks),
      authorizationBearer = token.access_token,
      addItemsEndpoint = spotifyConfig.endpoints.playlists.addItems
    ) match {
      case Right(_) =>
        Right(
          MusicRecommendation(
            Seq(
              MusicPlaylist(
                name = playlistName,
                service = "spotify",
                url = playlistUrl
              )
            )
          )
        )
      case Left(errorMessage) => Left(errorMessage)
    }

  object Defaults {
    val MaximumSeedTracks = 5
    val PlaylistName = "My Playlist"
  }
}
