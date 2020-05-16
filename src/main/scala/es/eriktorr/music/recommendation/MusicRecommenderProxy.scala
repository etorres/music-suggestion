package es.eriktorr.music.recommendation

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.ApplicationContextLoader
import es.eriktorr.music.aws.lambda.proxy.ApiGatewayProxy

final class MusicRecommenderProxy(private[this] val recommenderHandler: MusicRecommenderHandler)
    extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with ApiGatewayProxy[MusicFeatures, MusicRecommendation] {

  def this() = {
    this(
      recommenderHandler = new MusicRecommender(
        ApplicationContextLoader.applicationContext()
      )
    )
  }

  import MusicRecommenderJsonProtocol._

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = handleRequest(recommenderHandler, event, context)
}
