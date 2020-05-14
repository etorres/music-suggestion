package es.eriktorr.music.aws.lambda

import java.util

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import spray.json.{JsonFormat, _}

import scala.jdk.CollectionConverters._

trait LambdaProxy[I, O] {
  def handleRequest(
    handler: LambdaRequestHandler[I, O],
    event: APIGatewayProxyRequestEvent,
    context: Context
  )(
    implicit requestJsonFormat: JsonFormat[I],
    responseJsonFormat: JsonFormat[O]
  ): APIGatewayProxyResponseEvent = {
    val requestBody = Option(event.getBody).getOrElse(Defaults.EmptyJson)

    val responseBody =
      handler
        .handle(
          parameters =
            paramsFrom(event.getPathParameters) ++ paramsFrom(event.getQueryStringParameters),
          requestBody = requestBody.parseJson.convertTo[I],
          context = context
        )

    val responseHeaders = Map("x-api-gateway-proxy-footprint" -> this.getClass.getSimpleName)

    new APIGatewayProxyResponseEvent()
      .withStatusCode(200)
      .withHeaders(responseHeaders.asJava)
      .withBody(responseBody.toJson.compactPrint)
      .withIsBase64Encoded(false)
  }

  private[this] def paramsFrom(map: java.util.Map[String, String]): Map[String, String] =
    Option(map).getOrElse(new util.HashMap[String, String]()).asScala.toMap

  object Defaults {
    val EmptyJson = "{}"
  }
}
