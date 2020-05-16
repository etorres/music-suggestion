package es.eriktorr.music.recommendation

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.ApplicationContextLoader
import es.eriktorr.music.aws.lambda.proxy.ApiGatewayProxy

final class MusicRecommenderProxy
    extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with ApiGatewayProxy[MusicFeatures, MusicRecommendation] {

  private[this] val requestHandler = new MusicRecommender(
    ApplicationContextLoader.applicationContext()
  )
  import MusicRecommenderJsonProtocol._

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = handleRequest(requestHandler, event, context)
}
