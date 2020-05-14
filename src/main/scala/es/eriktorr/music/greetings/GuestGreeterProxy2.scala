package es.eriktorr.music.greetings

import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import es.eriktorr.music.aws.lambda.LambdaProxy
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object JsonProtocol2 extends DefaultJsonProtocol {
  implicit val requestFormat: RootJsonFormat[GreetingsRequest2] = jsonFormat1(GreetingsRequest2)
  implicit val responseFormat: RootJsonFormat[GreetingsResponse2] = jsonFormat1(GreetingsResponse2)
}

final class GuestGreeterProxy2
    extends RequestHandler[APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent]
    with LambdaProxy[GreetingsRequest2, GreetingsResponse2] {

  private[this] val requestHandler = new GuestGreeter2
  import JsonProtocol2._

  override def handleRequest(
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = handleRequest(requestHandler, event, context)
}
