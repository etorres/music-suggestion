package es.eriktorr.music.recommendation

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.ApplicationContextLoader
import es.eriktorr.music.aws.lambda.proxy.{ApiGatewayProxy, ApiGatewayRequestHandler}
import es.eriktorr.music.spotify.{
  SpotifyPlayerBackend,
  SpotifyPlaylistModifierBackend,
  SpotifyRecommenderBackend,
  SpotifyTokenRequesterBackend
}

final class MusicRecommenderProxy(
  private[this] val requestHandler: ApiGatewayRequestHandler[MusicFeatures, MusicRecommendation]
) extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with ApiGatewayProxy[MusicFeatures, MusicRecommendation] {

  def this() = {
    this(
      requestHandler = new MusicRecommender(
        applicationContext = ApplicationContextLoader.applicationContext(),
        spotifyTokenRequester = new SpotifyTokenRequesterBackend,
        spotifyPlayer = new SpotifyPlayerBackend,
        spotifyRecommender = new SpotifyRecommenderBackend,
        spotifyPlaylistModifier = new SpotifyPlaylistModifierBackend
      )
    )
  }

  import MusicRecommenderJsonProtocol._

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = handleRequest(requestHandler, event, context)
}
