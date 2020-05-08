package es.eriktorr.music

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

final class MainApiGatewayProxyHandler
    extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with BaseApiGatewayProxyHandler {
  private[this] val mainHandler = new MainHandler

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = handleRequest(mainHandler, event, context)
}
