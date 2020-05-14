package es.eriktorr.music.greetings

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.ApiGatewayProxyHandler

final class GuestGreeterProxy
    extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with ApiGatewayProxyHandler {
  private[this] val guestGreeter = new GuestGreeter

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent =
    handleRequest(guestGreeter, event, context)
}
