package es.eriktorr.music

import java.io.ByteArrayOutputStream

import better.files._
import com.amazonaws.services.lambda.runtime.events.{
  APIGatewayProxyRequestEvent,
  APIGatewayProxyResponseEvent
}
import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}

import scala.jdk.CollectionConverters._

trait ApiGatewayProxyHandler {
  def handleRequest(
    handler: RequestStreamHandler,
    event: APIGatewayProxyRequestEvent,
    context: Context
  ): APIGatewayProxyResponseEvent = {
    val requestBody = Option(event.getBody).getOrElse(Defaults.EmptyJson)

    val responseBody = (for {
      input <- requestBody.inputStream.autoClosed
      output <- new ByteArrayOutputStream().autoClosed
    } yield {
      handler.handleRequest(input, output, context)
      output.toString()
    }).get()

    val responseHeaders = Map("x-api-gateway-proxy-footprint" -> this.getClass.getSimpleName)

    new APIGatewayProxyResponseEvent()
      .withStatusCode(200)
      .withHeaders(responseHeaders.asJava)
      .withBody(responseBody)
      .withIsBase64Encoded(false)
  }

  object Defaults {
    val EmptyJson = "{}"
  }
}
