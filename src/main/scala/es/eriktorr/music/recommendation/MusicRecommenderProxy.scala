package es.eriktorr.music.recommendation

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.aws.lambda.LambdaProxy
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object MusicRecommendationJsonProtocol extends DefaultJsonProtocol {
  implicit val musicPlaylistFormat: RootJsonFormat[MusicPlaylist] = jsonFormat3(MusicPlaylist)
  implicit val requestFormat: RootJsonFormat[MusicFeatures] = jsonFormat8(MusicFeatures)
  implicit val responseFormat: RootJsonFormat[MusicRecommendation] = jsonFormat1(
    MusicRecommendation
  )
}

final class MusicRecommenderProxy
    extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with LambdaProxy[MusicFeatures, MusicRecommendation] {

  private[this] val requestHandler = new MusicRecommender
  import MusicRecommendationJsonProtocol._

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = handleRequest(requestHandler, event, context)
}
